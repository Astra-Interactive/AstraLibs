package ru.astrainteractive.astralibs.service

/**
 * A single unit of recurring work run by a [Service].
 *
 * Implementations must be cancellation-cooperative: the service cancels the running task when it is
 * stopped, restarted, or when the configured timeout elapses.
 */
fun interface ServiceTask {
    suspend fun execute()
}
