package ru.astrainteractive.astralibs.service

/**
 * Decides what happens when the next tick arrives while a [ServiceTask] is still running.
 */
enum class OverflowPolicy {
    /**
     * Wait for the running task, then run the pending tick.
     *
     * Produces a fixed *delay* between runs: the effective period is `interval + task duration`.
     */
    Await,

    /**
     * Drop the ticks that arrive while the task is busy and keep only the most recent one.
     *
     * Produces a fixed *rate* with skipping: the task never overlaps itself and never falls behind.
     */
    Conflate,

    /**
     * Cancel the running task and start it again for the new tick.
     *
     * Produces a fixed *rate* where only the newest run is allowed to finish.
     */
    CancelPrevious
}
