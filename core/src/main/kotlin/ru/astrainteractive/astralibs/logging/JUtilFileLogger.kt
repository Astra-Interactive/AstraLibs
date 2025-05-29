package ru.astrainteractive.astralibs.logging

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A file-based logger implementation that writes log entries to a specified folder.
 * It acts as a wrapper for an underlying [Logger] instance, extending its capabilities
 * by directing logs to files.
 *
 * @param folder The directory where log files will be saved.
 * @param instance The underlying [Logger] instance responsible for log formatting and management.
 */
class JUtilFileLogger(
    private val folder: File,
    private val instance: Logger
) : Logger {
    override val TAG: String = instance.TAG

    /**
     * Returns current time in HH:mm:ss format
     */
    private fun getTime(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        return formatter.format(now)
    }

    /**
     * Returns current date in yyyy-MM-dd format
     */
    private fun getDate(): String {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return formatter.format(now)
    }

    override fun debug(logMessage: () -> String) {
        instance.debug(logMessage)
        logInFile(logMessage)
    }

    override fun error(logMessage: () -> String) {
        instance.error(logMessage)
        logInFile(logMessage)
    }

    override fun info(logMessage: () -> String) {
        instance.info(logMessage)
        logInFile(logMessage)
    }

    override fun verbose(logMessage: () -> String) {
        instance.verbose(logMessage)
        logInFile(logMessage)
    }

    override fun warn(logMessage: () -> String) {
        instance.warn(logMessage)
        logInFile(logMessage)
    }

    override fun error(error: Throwable?, logMessage: () -> String) {
        instance.error(logMessage)
        error?.stackTraceToString()?.let { stackTraceToString ->
            logInFile { stackTraceToString }
        }
        logInFile(logMessage)
    }

    private fun getLogFile(): File {
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val data = getDate()
        val lastIndex = folder.listFiles().orEmpty()
            .filter { it.nameWithoutExtension.startsWith(data) }
            .maxOfOrNull {
                if (!it.nameWithoutExtension.contains("-")) {
                    0
                } else {
                    it.nameWithoutExtension.split("-").lastOrNull()?.toIntOrNull() ?: 0
                }
            }
        val lastFile = File(folder, "$data-$lastIndex.log")
        if (!lastFile.exists()) {
            lastFile.createNewFile()
            return lastFile
        }
        val fileSizeMegaBytes = lastFile.length() / 1024f / 1024f
        return if (fileSizeMegaBytes > MAX_FILE_SIZE_MB) {
            val newFile = File(folder, "$data-$lastIndex.log")
            newFile.createNewFile()
            newFile
        } else {
            lastFile
        }
    }

    private fun logInFile(logMessage: () -> String) {
        val time = getTime()
        val file = getLogFile()
        file.appendText("[$time] [$TAG]: ${logMessage.invoke()}\n")
    }

    companion object {
        private const val MAX_FILE_SIZE_MB = 8 * 1024 * 1024

        /**
         * Convert default [Logger] into [JUtilFileLogger]
         */
        fun Logger.toFileLogger(folder: File): JUtilFileLogger {
            check(this !is JUtilFileLogger) {
                "${this::class.java} already JUtilFileLogger!"
            }
            return JUtilFileLogger(folder, this)
        }
    }
}
