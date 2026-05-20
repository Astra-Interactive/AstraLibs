package ru.astrainteractive.astralibs.service

/** Recurring-work contract invoked by [TickFlowService] on each tick. */
fun interface ServiceExecutor {
    suspend fun doWork()
}
