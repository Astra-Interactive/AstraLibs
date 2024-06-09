package ru.astrainteractive.astralibs.logging

/**
 * Use [Logger] with delegation
 */
interface Logger {
    @Suppress("VariableNaming")
    val TAG: String

    fun error(logMessage: () -> String)

    fun error(error: Throwable?, logMessage: () -> String)

    fun info(logMessage: () -> String)

    fun verbose(logMessage: () -> String)

    fun warn(logMessage: () -> String)

    fun debug(logMessage: () -> String)
}
