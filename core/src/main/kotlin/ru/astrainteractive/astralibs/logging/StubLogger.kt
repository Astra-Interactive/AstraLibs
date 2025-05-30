package ru.astrainteractive.astralibs.logging

/**
 * A no-op implementation of [Logger] that ignores all log messages.
 *
 * This is useful for testing, default fallbacks, or disabling logging entirely
 * without null checks or conditional logging.
 */
object StubLogger : Logger {
    override val TAG: String = "StubLogger"

    override fun error(logMessage: () -> String) = Unit

    override fun error(error: Throwable?, logMessage: () -> String) = Unit

    override fun info(logMessage: () -> String) = Unit

    override fun verbose(logMessage: () -> String) = Unit

    override fun warn(logMessage: () -> String) = Unit

    override fun debug(logMessage: () -> String) = Unit
}
