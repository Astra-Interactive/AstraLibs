package ru.astrainteractive.astralibs.service

import ru.astrainteractive.klibs.mikro.core.logging.Logger

/** [Logger] that evaluates every message lambda, exactly like a real logger would, and records it. */
class FakeLogger : Logger {
    @Suppress("VariableNaming")
    override val TAG: String = "FakeLogger"

    val errors = mutableListOf<Throwable?>()
    val errorMessages = mutableListOf<String>()
    val warnings = mutableListOf<String>()

    override fun error(logMessage: () -> String) {
        errors.add(null)
        errorMessages.add(logMessage.invoke())
    }

    override fun error(error: Throwable?, logMessage: () -> String) {
        errors.add(error)
        errorMessages.add(logMessage.invoke())
    }

    override fun warn(logMessage: () -> String) {
        warnings.add(logMessage.invoke())
    }

    override fun info(logMessage: () -> String) = Unit

    override fun verbose(logMessage: () -> String) = Unit

    override fun debug(logMessage: () -> String) = Unit
}
