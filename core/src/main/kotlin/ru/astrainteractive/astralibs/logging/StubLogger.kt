package ru.astrainteractive.astralibs.logging

object StubLogger: Logger {
    override val TAG: String = "StubLogger"

    override fun error(logMessage: () -> String) = Unit

    override fun error(error: Throwable?, logMessage: () -> String) = Unit

    override fun info(logMessage: () -> String) = Unit

    override fun verbose(logMessage: () -> String) = Unit

    override fun warn(logMessage: () -> String) = Unit

    override fun debug(logMessage: () -> String) = Unit
}