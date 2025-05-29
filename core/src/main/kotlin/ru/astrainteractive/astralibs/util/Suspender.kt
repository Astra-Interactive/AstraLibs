package ru.astrainteractive.astralibs.util

import java.time.Clock
import java.time.Instant
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * A utility class that manages a simple suspension mechanism based on a duration.
 *
 * Once [setSuspended] is called, the suspender enters a suspended state for the
 * specified [duration]. During this period, [isSuspended] will return `true`.
 * After the duration has elapsed, [isSuspended] will return `false`.
 *
 * Example usage:
 * ```
 * val suspender = Suspender(Duration.seconds(30))
 * suspender.setSuspended()
 * if (suspender.isSuspended()) {
 *     println("Still suspended.")
 * }
 * ```
 *
 * @property duration The duration for which the object remains suspended after being triggered.
 */
class Suspender(
    private val duration: Duration,
    private val clock: Clock = Clock.systemUTC()
) {
    private var lastSuspendedAt: Instant = Instant.EPOCH

    /**
     * Checks whether the current time is still within the suspension period.
     *
     * @return `true` if the suspender is still within the active suspension window, `false` otherwise.
     */
    fun isSuspended(): Boolean {
        val now = clock.instant()
        return lastSuspendedAt.plus(duration.toJavaDuration()).isAfter(now)
    }

    /**
     * Marks the current moment as the start of the suspension period.
     */
    fun setSuspended() {
        lastSuspendedAt = clock.instant()
    }
}
