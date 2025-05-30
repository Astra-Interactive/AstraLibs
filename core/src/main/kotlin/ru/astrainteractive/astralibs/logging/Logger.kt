package ru.astrainteractive.astralibs.logging

/**
 * Interface for structured and leveled logging with lazy message evaluation.
 *
 * Provides common logging levels such as error, warning, info, debug, and verbose.
 * All log messages are passed as lambda expressions for lazy evaluation.
 */
interface Logger {

    /**
     * A tag identifying the logger source (e.g., class or module name).
     */
    @Suppress("VariableNaming")
    val TAG: String

    /**
     * Logs an error-level message.
     *
     * @param logMessage A lambda that returns the message to log.
     */
    fun error(logMessage: () -> String)

    /**
     * Logs an error-level message with an optional throwable.
     *
     * @param error An optional [Throwable] to log alongside the message.
     * @param logMessage A lambda that returns the message to log.
     */
    fun error(error: Throwable?, logMessage: () -> String)

    /**
     * Logs an info-level message.
     *
     * @param logMessage A lambda that returns the message to log.
     */
    fun info(logMessage: () -> String)

    /**
     * Logs a verbose-level message, typically used for fine-grained diagnostics.
     *
     * @param logMessage A lambda that returns the message to log.
     */
    fun verbose(logMessage: () -> String)

    /**
     * Logs a warning-level message.
     *
     * @param logMessage A lambda that returns the message to log.
     */
    fun warn(logMessage: () -> String)

    /**
     * Logs a debug-level message.
     *
     * @param logMessage A lambda that returns the message to log.
     */
    fun debug(logMessage: () -> String)
}
