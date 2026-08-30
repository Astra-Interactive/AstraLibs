package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.Collections
import kotlin.concurrent.thread
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Suppress("TestFunctionName", "LargeClass")
class IntervalServiceTest {

    // region validation

    @Test
    fun GIVEN_invalid_initial_delay_WHEN_service_is_enabled_THEN_it_starts_without_a_delay_and_warns() = runTest {
        listOf((-1).seconds, Duration.INFINITE).forEach { invalidDelay ->
            val logger = FakeLogger()
            val startTimes = mutableListOf<Long>()
            val start = currentTime
            val service = IntervalService(
                interval = flowOf(100.milliseconds),
                getInitialDelay = { invalidDelay },
                scope = backgroundScope,
                logger = logger,
                task = { startTimes.add(currentTime - start) }
            )

            service.onEnable()
            advanceTimeBy(150.milliseconds)
            service.onDisable()
            service.awaitIdle()

            assertContentEquals(listOf(0L, 100L), startTimes, "initial delay $invalidDelay")
            assertTrue(logger.warnings.any { warning -> warning.contains("invalid initial delay") })
        }
    }

    @Test
    fun GIVEN_failing_initial_delay_provider_WHEN_service_is_enabled_THEN_it_starts_and_reports() = runTest {
        val logger = FakeLogger()
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            getInitialDelay = { error("config is not loaded yet") },
            scope = backgroundScope,
            logger = logger,
            task = task
        )

        service.onEnable()
        advanceTimeBy(150.milliseconds)

        assertEquals(2, task.started)
        assertEquals(1, logger.errors.size)
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_changed_initial_delay_WHEN_service_is_reloaded_THEN_the_new_value_is_used() = runTest {
        var configuredDelay = 70.milliseconds
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(1.seconds),
            getInitialDelay = { configuredDelay },
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(100.milliseconds)
        configuredDelay = 500.milliseconds
        service.onReload()
        advanceTimeBy(700.milliseconds)

        assertContentEquals(listOf(70L, 600L), startTimes)
    }

    @Test
    fun GIVEN_non_positive_timeout_WHEN_service_is_created_THEN_it_is_rejected() {
        listOf(Duration.ZERO, (-1).seconds).forEach { badTimeout ->
            assertFailsWith<IllegalArgumentException> {
                IntervalService(
                    interval = flowOf(1.seconds),
                    timeout = badTimeout,
                    scope = CoroutineScope(SupervisorJob()),
                    logger = FakeLogger(),
                    task = FakeServiceTask()
                )
            }
        }
    }

    @Test
    fun GIVEN_infinite_timeout_WHEN_task_runs_longer_than_any_bound_THEN_it_is_not_cancelled() = runTest {
        val logger = FakeLogger()
        val task = FakeServiceTask(duration = 10.seconds)
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            timeout = Duration.INFINITE,
            scope = backgroundScope,
            logger = logger,
            task = task
        )

        service.onEnable()
        advanceTimeBy(11.seconds)

        assertEquals(1, task.finished)
        assertTrue(logger.warnings.isEmpty())
    }

    // endregion

    // region scheduling

    @Test
    fun GIVEN_created_service_WHEN_it_is_never_enabled_THEN_task_never_runs() = runTest {
        val task = FakeServiceTask()
        IntervalService(
            interval = flowOf(10.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        advanceTimeBy(1.seconds)

        assertEquals(0, task.started)
    }

    @Test
    fun GIVEN_enabled_service_WHEN_interval_elapses_THEN_task_runs_once_per_interval() = runTest {
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertContentEquals(listOf(0L, 100L, 200L), startTimes)
    }

    @Test
    fun GIVEN_initial_delay_WHEN_service_is_enabled_THEN_first_run_waits_for_it() = runTest {
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            getInitialDelay = { 70.milliseconds },
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertContentEquals(listOf(70L, 170L), startTimes)
    }

    @Test
    fun GIVEN_initial_delay_WHEN_service_is_reloaded_THEN_it_is_applied_again() = runTest {
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            getInitialDelay = { 70.milliseconds },
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(100.milliseconds)
        service.onReload()
        advanceTimeBy(100.milliseconds)

        assertContentEquals(listOf(70L, 170L), startTimes)
    }

    @Test
    fun GIVEN_fixed_interval_constructor_WHEN_enabled_THEN_task_runs_on_that_interval() = runTest {
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = 100.milliseconds,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertContentEquals(listOf(0L, 100L, 200L), startTimes)
    }

    // endregion

    // region reactive interval

    @Test
    fun GIVEN_running_service_WHEN_interval_changes_THEN_new_interval_applies_immediately() = runTest {
        val interval = MutableStateFlow(100.milliseconds)
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = interval,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)
        interval.value = 20.milliseconds
        advanceTimeBy(60.milliseconds)

        assertContentEquals(listOf(0L, 100L, 200L, 250L, 270L, 290L), startTimes)
    }

    @Test
    fun GIVEN_running_service_WHEN_same_interval_is_emitted_again_THEN_schedule_is_not_restarted() = runTest {
        val interval = MutableSharedFlow<Duration>(replay = 1)
        interval.tryEmit(100.milliseconds)
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = interval,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(150.milliseconds)
        interval.tryEmit(100.milliseconds)
        advanceTimeBy(100.milliseconds)

        assertContentEquals(listOf(0L, 100L, 200L), startTimes)
    }

    @Test
    fun GIVEN_non_positive_interval_WHEN_it_is_emitted_THEN_task_never_runs_and_warning_is_reported() = runTest {
        val logger = FakeLogger()
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = MutableStateFlow(Duration.ZERO),
            scope = backgroundScope,
            logger = logger,
            task = task
        )

        service.onEnable()
        advanceTimeBy(1.seconds)

        assertEquals(0, task.started)
        assertEquals(Service.State.Active, service.state.value)
        assertTrue(logger.warnings.any { warning -> warning.contains("non-positive") })
    }

    @Test
    fun GIVEN_non_positive_interval_WHEN_positive_one_arrives_THEN_ticking_resumes() = runTest {
        val interval = MutableStateFlow((-5).milliseconds)
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = interval,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        advanceTimeBy(500.milliseconds)
        interval.value = 100.milliseconds
        advanceTimeBy(150.milliseconds)

        assertContentEquals(listOf(500L, 600L), startTimes)
    }

    // endregion

    // region overflow policies

    @Test
    fun GIVEN_await_policy_WHEN_task_is_slow_THEN_runs_are_spaced_by_interval_plus_duration() = runTest {
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            overflowPolicy = OverflowPolicy.Await,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = {
                startTimes.add(currentTime)
                delay(30.milliseconds)
            }
        )

        service.onEnable()
        advanceTimeBy(300.milliseconds)

        assertContentEquals(listOf(0L, 130L, 260L), startTimes)
    }

    @Test
    fun GIVEN_conflate_policy_WHEN_task_is_slower_than_interval_THEN_runs_keep_the_interval_rate() = runTest {
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            overflowPolicy = OverflowPolicy.Conflate,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = {
                startTimes.add(currentTime)
                delay(30.milliseconds)
            }
        )

        service.onEnable()
        advanceTimeBy(300.milliseconds)

        assertContentEquals(listOf(0L, 100L, 200L), startTimes)
    }

    @Test
    fun GIVEN_conflate_policy_WHEN_many_ticks_pile_up_THEN_they_collapse_into_a_single_run() = runTest {
        val task = FakeServiceTask(duration = 500.milliseconds)
        val service = IntervalService(
            interval = flowOf(50.milliseconds),
            overflowPolicy = OverflowPolicy.Conflate,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        advanceTimeBy(1050.milliseconds)

        assertEquals(3, task.started)
        assertEquals(2, task.finished)
    }

    @Test
    fun GIVEN_cancel_previous_policy_WHEN_next_tick_arrives_THEN_running_task_is_cancelled() = runTest {
        val task = FakeServiceTask(duration = 500.milliseconds)
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            overflowPolicy = OverflowPolicy.CancelPrevious,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        advanceTimeBy(450.milliseconds)

        assertEquals(5, task.started)
        assertEquals(0, task.finished)
    }

    @Test
    fun GIVEN_cancel_previous_policy_WHEN_task_fits_the_interval_THEN_every_run_completes() = runTest {
        val task = FakeServiceTask(duration = 30.milliseconds)
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            overflowPolicy = OverflowPolicy.CancelPrevious,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertEquals(3, task.started)
        assertEquals(3, task.finished)
    }

    @Test
    fun GIVEN_any_policy_WHEN_task_is_slower_than_interval_THEN_runs_never_overlap() = runTest {
        OverflowPolicy.entries.forEach { policy ->
            val task = FakeServiceTask(duration = 500.milliseconds)
            val service = IntervalService(
                interval = flowOf(50.milliseconds),
                overflowPolicy = policy,
                scope = backgroundScope,
                logger = FakeLogger(),
                task = task
            )

            service.onEnable()
            advanceTimeBy(2.seconds)
            service.onDisable()
            runCurrent()

            assertEquals(1, task.maxConcurrent, "policy $policy overlapped runs")
        }
    }

    // endregion

    // region failures and timeouts

    @Test
    fun GIVEN_failing_task_WHEN_it_throws_THEN_service_keeps_running_and_error_is_reported() = runTest {
        val logger = FakeLogger()
        val task = FakeServiceTask(failEveryNthRun = 2)
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = logger,
            task = task
        )

        service.onEnable()
        advanceTimeBy(450.milliseconds)

        assertEquals(5, task.started)
        assertEquals(3, task.finished)
        assertEquals(2, logger.errors.size)
        assertTrue(logger.errorMessages.all { message -> message.contains("keeps running") })
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_running_task_WHEN_service_is_disabled_THEN_cancellation_is_not_reported_as_failure() = runTest {
        val logger = FakeLogger()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = logger,
            task = { delay(1.seconds) }
        )

        service.onEnable()
        advanceTimeBy(50.milliseconds)
        service.onDisable()
        service.awaitIdle()

        assertTrue(logger.errors.isEmpty())
        assertEquals(Service.State.Idle, service.state.value)
    }

    @Test
    fun GIVEN_task_exceeding_timeout_WHEN_it_hangs_THEN_it_is_cancelled_and_schedule_continues() = runTest {
        val logger = FakeLogger()
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            timeout = 50.milliseconds,
            scope = backgroundScope,
            logger = logger,
            task = {
                startTimes.add(currentTime)
                delay(Duration.INFINITE)
            }
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertContentEquals(listOf(0L, 150L), startTimes)
        assertEquals(2, logger.warnings.count { warning -> warning.contains("did not finish") })
        assertTrue(logger.errors.isEmpty())
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_task_within_timeout_WHEN_it_completes_THEN_no_timeout_is_reported() = runTest {
        val logger = FakeLogger()
        val task = FakeServiceTask(duration = 10.milliseconds)
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            timeout = 50.milliseconds,
            scope = backgroundScope,
            logger = logger,
            task = task
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertEquals(3, task.finished)
        assertTrue(logger.warnings.isEmpty())
    }

    @Test
    fun GIVEN_failing_task_and_a_timeout_WHEN_it_throws_THEN_it_is_reported_as_a_failure_not_a_timeout() = runTest {
        val logger = FakeLogger()
        val task = FakeServiceTask(failEveryNthRun = 1)
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            timeout = 50.milliseconds,
            scope = backgroundScope,
            logger = logger,
            task = task
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertEquals(3, task.started)
        assertEquals(3, logger.errors.size)
        assertTrue(logger.warnings.isEmpty())
    }

    @Test
    fun GIVEN_interval_that_never_emits_WHEN_service_is_enabled_THEN_it_stays_active_without_running() = runTest {
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = MutableSharedFlow(),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        advanceTimeBy(1.seconds)

        assertEquals(0, task.started)
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_await_policy_WHEN_interval_changes_mid_run_THEN_the_run_in_flight_is_cancelled() = runTest {
        val interval = MutableStateFlow(100.milliseconds)
        val task = FakeServiceTask(duration = 500.milliseconds)
        val service = IntervalService(
            interval = interval,
            overflowPolicy = OverflowPolicy.Await,
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        advanceTimeBy(100.milliseconds)
        interval.value = 50.milliseconds
        advanceTimeBy(10.milliseconds)

        assertEquals(2, task.started)
        assertEquals(0, task.finished)
        assertEquals(1, task.maxConcurrent)
    }

    @Test
    fun GIVEN_interval_that_completes_without_a_value_WHEN_service_is_enabled_THEN_it_becomes_idle_again() = runTest {
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = emptyFlow(),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        runCurrent()

        assertEquals(0, task.started)
        assertEquals(Service.State.Idle, service.state.value)
    }

    // endregion

    // region lifecycle

    @Test
    fun GIVEN_enabled_service_WHEN_enabled_again_THEN_schedule_is_not_duplicated() = runTest {
        val startTimes = mutableListOf<Long>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = { startTimes.add(currentTime) }
        )

        service.onEnable()
        service.onEnable()
        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertContentEquals(listOf(0L, 100L, 200L), startTimes)
    }

    @Test
    fun GIVEN_never_enabled_service_WHEN_disabled_THEN_it_stays_idle_and_task_never_runs() = runTest {
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = flowOf(10.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onDisable()
        service.onDisable()
        advanceTimeBy(1.seconds)

        assertEquals(Service.State.Idle, service.state.value)
        assertEquals(0, task.started)
    }

    @Test
    fun GIVEN_disabled_service_WHEN_enabled_again_THEN_task_runs_again() = runTest {
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)
        service.onDisable()
        runCurrent()
        val afterFirstRun = task.started

        service.onEnable()
        advanceTimeBy(250.milliseconds)

        assertEquals(3, afterFirstRun)
        assertEquals(6, task.started)
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_active_service_WHEN_reloaded_THEN_it_keeps_running() = runTest {
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onEnable()
        advanceTimeBy(250.milliseconds)
        val beforeReload = task.started
        service.onReload()
        advanceTimeBy(250.milliseconds)

        assertTrue(task.started > beforeReload)
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_never_enabled_service_WHEN_reloaded_THEN_it_starts() = runTest {
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = task
        )

        service.onReload()
        advanceTimeBy(250.milliseconds)

        assertEquals(3, task.started)
        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_disabled_service_WHEN_task_is_still_unwinding_THEN_state_stays_active_until_it_finishes() = runTest {
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = {
                try {
                    delay(1.seconds)
                } finally {
                    withContext(NonCancellable) { delay(100.milliseconds) }
                }
            }
        )

        service.onEnable()
        advanceTimeBy(50.milliseconds)
        service.onDisable()
        advanceTimeBy(50.milliseconds)

        assertEquals(Service.State.Active, service.state.value)

        advanceTimeBy(100.milliseconds)

        assertEquals(Service.State.Idle, service.state.value)
    }

    @Test
    fun GIVEN_stopped_service_WHEN_await_idle_THEN_it_returns_after_the_task_unwinds() = runTest {
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = {
                try {
                    delay(1.seconds)
                } finally {
                    withContext(NonCancellable) { delay(100.milliseconds) }
                }
            }
        )

        service.onEnable()
        advanceTimeBy(50.milliseconds)
        service.onDisable()
        service.awaitIdle()

        assertEquals(Service.State.Idle, service.state.value)
        assertEquals(150L, currentTime)
    }

    // endregion

    // region scope ownership

    @Test
    fun GIVEN_cancelled_scope_WHEN_service_is_enabled_THEN_it_does_not_start_and_reports_a_warning() = runTest {
        val logger = FakeLogger()
        val task = FakeServiceTask()
        val deadScope = CoroutineScope(SupervisorJob())
        deadScope.cancel()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = deadScope,
            logger = logger,
            task = task
        )

        service.onEnable()
        advanceTimeBy(1.seconds)

        assertEquals(0, task.started)
        assertEquals(Service.State.Idle, service.state.value)
        assertTrue(logger.warnings.any { warning -> warning.contains("scope is already cancelled") })
    }

    @Test
    fun GIVEN_shared_scope_WHEN_one_service_is_disabled_THEN_the_other_keeps_running() = runTest {
        val firstTask = FakeServiceTask()
        val secondTask = FakeServiceTask()
        val first = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = firstTask
        )
        val second = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = secondTask
        )

        first.onEnable()
        second.onEnable()
        advanceTimeBy(250.milliseconds)
        first.onDisable()
        runCurrent()
        val firstAfterStop = firstTask.started
        advanceTimeBy(250.milliseconds)

        assertEquals(firstAfterStop, firstTask.started)
        assertEquals(5, secondTask.started)
        assertEquals(Service.State.Active, second.state.value)
    }

    @Test
    fun GIVEN_externally_cancelled_scope_WHEN_task_is_running_THEN_service_becomes_idle() = runTest {
        val scope = CoroutineScope(SupervisorJob() + backgroundScope.coroutineContext)
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = scope,
            logger = FakeLogger(),
            task = { delay(1.seconds) }
        )

        service.onEnable()
        advanceTimeBy(50.milliseconds)
        scope.cancel()
        runCurrent()

        assertEquals(Service.State.Idle, service.state.value)
    }

    // endregion

    // region restart races

    @Test
    fun GIVEN_reloaded_service_WHEN_previous_run_completes_late_THEN_state_is_not_reset_to_idle() = runTest {
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = {
                try {
                    delay(1.seconds)
                } finally {
                    withContext(NonCancellable) { delay(200.milliseconds) }
                }
            }
        )

        service.onEnable()
        advanceTimeBy(50.milliseconds)
        service.onReload()
        advanceTimeBy(300.milliseconds)

        assertEquals(Service.State.Active, service.state.value)
    }

    @Test
    fun GIVEN_reloaded_service_WHEN_previous_run_is_unwinding_THEN_new_run_does_not_overlap_it() = runTest {
        val events = mutableListOf<String>()
        val service = IntervalService(
            interval = flowOf(100.milliseconds),
            scope = backgroundScope,
            logger = FakeLogger(),
            task = {
                events.add("start@$currentTime")
                try {
                    delay(1.seconds)
                } finally {
                    withContext(NonCancellable) { delay(200.milliseconds) }
                    events.add("end@$currentTime")
                }
            }
        )

        service.onEnable()
        advanceTimeBy(50.milliseconds)
        service.onReload()
        advanceTimeBy(300.milliseconds)

        assertContentEquals(listOf("start@0", "end@250", "start@250"), events)
    }

    @Test
    fun GIVEN_many_threads_WHEN_lifecycle_calls_race_THEN_service_stays_consistent() {
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
        val task = FakeServiceTask()
        val service = IntervalService(
            interval = 1.milliseconds,
            scope = scope,
            logger = FakeLogger(),
            task = task
        )
        val failures = Collections.synchronizedList(mutableListOf<Throwable>())

        val threads = (1..8).map { index ->
            thread {
                repeat(500) {
                    runCatching {
                        when (index % 3) {
                            0 -> service.onEnable()
                            1 -> service.onDisable()
                            else -> service.onReload()
                        }
                    }.onFailure { throwable -> failures.add(throwable) }
                }
            }
        }
        threads.forEach { worker -> worker.join() }

        service.onDisable()
        runBlocking {
            withTimeout(5.seconds) { service.awaitIdle() }

            assertTrue(failures.isEmpty(), "lifecycle calls threw: ${failures.firstOrNull()}")
            assertEquals(Service.State.Idle, service.state.value)

            val beforeRestart = task.started
            service.onEnable()
            delay(200.milliseconds)

            assertTrue(task.started > beforeRestart, "service was not reusable after the race")
        }
        scope.cancel()
    }

    // endregion
}
