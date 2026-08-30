package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import ru.astrainteractive.klibs.mikro.core.coroutines.TickFlow
import ru.astrainteractive.klibs.mikro.core.logging.Logger
import kotlin.time.Duration

/**
 * [Service] that runs [task] repeatedly, once per [interval].
 *
 * - A new [interval] value is applied immediately and starts a fresh tick; repeated equal values
 *   are ignored, and non-positive ones are skipped with a warning instead of spinning. Under
 *   [OverflowPolicy.Await] that restart also cancels the run in flight.
 * - [getInitialDelay] is queried on every start, including after [Service.onReload], so a
 *   reloaded config takes effect without recreating the service.
 * - A failing [task] is reported through [logger] and never stops the service.
 * - A [task] that outlives [timeout] is cancelled, reported, and the schedule continues.
 * - [scope] is caller-owned: the service launches a child job in it and cancels only that child,
 *   never [scope] itself.
 * - [task] runs on [scope]'s dispatcher, so it must not touch thread-confined server APIs unless
 *   that dispatcher is the main one.
 *
 * Only [timeout] is validated eagerly. Values that arrive at runtime, through [interval] or
 * [getInitialDelay], are skipped or coerced with a warning, and a [getInitialDelay] that throws is
 * reported, so a bad config can never kill the service.
 */
class IntervalService(
    private val interval: Flow<Duration>,
    private val scope: CoroutineScope,
    private val logger: Logger,
    private val task: ServiceTask,
    private val getInitialDelay: () -> Duration = { Duration.ZERO },
    private val overflowPolicy: OverflowPolicy = OverflowPolicy.Await,
    private val timeout: Duration = Duration.INFINITE,
) : Service {
    private var job: Job? = null
    private val lock = Any()
    private val mutableState = MutableStateFlow(Service.State.Idle)
    override val state: StateFlow<Service.State> = mutableState.asStateFlow()

    init {
        require(timeout > Duration.ZERO) { "timeout must be positive, but was $timeout" }
    }

    constructor(
        interval: Duration,
        scope: CoroutineScope,
        logger: Logger,
        task: ServiceTask
    ) : this(
        interval = flowOf(interval),
        scope = scope,
        logger = logger,
        task = task
    )

    private fun getInitialDelaySafe(): Duration {
        val delay = runCatching { getInitialDelay.invoke() }.fold(
            onSuccess = { value -> value },
            onFailure = { throwable ->
                logger.error(throwable) { "Initial delay provider failed, starting without a delay" }
                Duration.ZERO
            }
        )
        if (delay.isFinite() && !delay.isNegative()) return delay
        logger.warn { "Ignoring invalid initial delay: $delay" }
        return Duration.ZERO
    }

    private suspend fun executeWithTimeout() {
        if (timeout.isInfinite()) {
            task.execute()
            return
        }
        val completed = withTimeoutOrNull(timeout) { task.execute() }
        if (completed == null) logger.warn { "Task did not finish in $timeout and was cancelled" }
    }

    private suspend fun runTask() {
        runCatching { executeWithTimeout() }
            .onFailure { throwable ->
                if (throwable is CancellationException) throw throwable
                logger.error(throwable) { "Task failed, the service keeps running" }
            }
    }

    private suspend fun runFixedDelaySchedule(period: Duration) {
        while (currentCoroutineContext().isActive) {
            runTask()
            delay(period)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun runSchedule() {
        val periods = interval
            .distinctUntilChanged()
            .filter { period ->
                val isPositive = period > Duration.ZERO
                if (!isPositive) logger.warn { "Ignoring non-positive interval: $period" }
                isPositive
            }
        when (overflowPolicy) {
            // The delay has to follow the run, so the tick must not be handed over through a
            // channel: any channel resumes the producer on hand-off, not on completion of the work.
            OverflowPolicy.Await -> periods.collectLatest { period -> runFixedDelaySchedule(period) }

            OverflowPolicy.Conflate ->
                periods.flatMapLatest { period -> TickFlow(duration = period) }
                    .conflate()
                    .collect { runTask() }

            OverflowPolicy.CancelPrevious ->
                periods.flatMapLatest { period -> TickFlow(duration = period) }
                    .collectLatest { runTask() }
        }
    }

    private fun onJobCompleted(completed: Job) {
        synchronized(lock) {
            // A restart may have already replaced this job; only the current one owns the state.
            if (job !== completed) return
            job = null
            mutableState.value = Service.State.Idle
        }
    }

    override fun onEnable() {
        synchronized(lock) {
            if (job?.isActive == true) return
            if (!scope.isActive) {
                logger.warn { "Not starting: the provided scope is already cancelled" }
                return
            }
            val previous = job
            val current = scope.launch {
                // A restart must not overlap with the run it replaces.
                previous?.join()
                delay(getInitialDelaySafe())
                runSchedule()
            }
            job = current
            mutableState.value = Service.State.Active
            current.invokeOnCompletion { onJobCompleted(current) }
        }
    }

    override fun onDisable() {
        synchronized(lock) {
            job?.cancel()
        }
    }
}
