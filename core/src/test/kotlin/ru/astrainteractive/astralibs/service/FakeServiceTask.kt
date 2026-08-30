package ru.astrainteractive.astralibs.service

import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.Duration

/**
 * [ServiceTask] that records how it was run.
 *
 * [maxConcurrent] stays at 1 as long as the service never overlaps two runs, and [finished] lags
 * behind [started] whenever a run is cancelled or fails. Counters are atomic so the fake can also
 * be used from tests that hit the service from several threads.
 */
class FakeServiceTask(
    private val duration: Duration = Duration.ZERO,
    private val failEveryNthRun: Int = 0
) : ServiceTask {
    private val concurrentCount = AtomicInteger(0)
    private val startedCount = AtomicInteger(0)
    private val finishedCount = AtomicInteger(0)
    private val maxConcurrentCount = AtomicInteger(0)

    val started: Int
        get() = startedCount.get()

    val finished: Int
        get() = finishedCount.get()

    val maxConcurrent: Int
        get() = maxConcurrentCount.get()

    override suspend fun execute() {
        val run = startedCount.incrementAndGet()
        val active = concurrentCount.incrementAndGet()
        maxConcurrentCount.updateAndGet { current -> maxOf(current, active) }
        try {
            if (duration > Duration.ZERO) delay(duration)
            if (failEveryNthRun > 0 && run % failEveryNthRun == 0) error("Task failed on run $run")
            finishedCount.incrementAndGet()
        } finally {
            concurrentCount.decrementAndGet()
        }
    }
}
