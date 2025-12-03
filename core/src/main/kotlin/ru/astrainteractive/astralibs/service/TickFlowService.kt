package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.astrainteractive.astralibs.coroutines.withTimings
import ru.astrainteractive.klibs.mikro.core.coroutines.CoroutineFeature
import ru.astrainteractive.klibs.mikro.core.coroutines.TickFlow
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration

class TickFlowService(
    coroutineContext: CoroutineContext = Dispatchers.IO,
    private val delay: Flow<Duration>,
    private val initialDelay: Flow<Duration> = flowOf(Duration.ZERO),
    private val executor: ServiceExecutor
) : Service {
    private val scope = CoroutineFeature
        .Default(coroutineContext)
        .withTimings()

    private val lazyTickFlow = lazy {
        combine(delay, initialDelay, ::TickFlow)
            .flattenConcat()
            .onEach { executor.doWork() }
            .launchIn(scope)
    }

    override fun onCreate() {
        lazyTickFlow.value
    }

    override fun onDestroy() {
        lazyTickFlow.value.cancel()
        scope.cancel()
    }
}
