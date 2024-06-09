package ru.astrainteractive.astralibs.logging

import java.util.logging.Level
import java.util.logging.Logger as JLogger

/**
 * This is default implementation with [JLogger]
 */
class JUtiltLogger(
    override val TAG: String,
    private val logger: JLogger = JLogger.getLogger(TAG)
) : Logger {

    override fun error(logMessage: () -> String) {
        logger.log(Level.SEVERE, logMessage)
    }

    override fun error(error: Throwable?, logMessage: () -> String) {
        logger.log(Level.SEVERE, error, logMessage)
    }

    override fun info(logMessage: () -> String) {
        logger.info(logMessage)
    }

    override fun verbose(logMessage: () -> String) {
        logger.log(Level.FINE, logMessage)
    }

    override fun warn(logMessage: () -> String) {
        logger.log(Level.WARNING, logMessage)
    }

    override fun debug(logMessage: () -> String) {
        logger.log(Level.ALL, logMessage)
    }
}
