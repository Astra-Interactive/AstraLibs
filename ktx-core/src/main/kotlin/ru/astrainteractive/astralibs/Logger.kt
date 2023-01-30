package ru.astrainteractive.astralibs

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Logger file
 */
object Logger {
    /**
     * Custom log path
     */
    var logsFolderPath: String? = null

    /**
     * Custom logger implementation
     */
    var logger: Logger? = null
    var prefix = "[AstraLibs]"
        set(value) {
            field = "[$value]"
        }


    /**
     * Log message with tag
     * @see Type
     */
    fun log(tag: String, message: String, consolePrint: Boolean = true) {
        log(tag, message, Level.INFO, consolePrint)
    }

    fun warn(tag: String, message: String, consolePrint: Boolean = true) {
        log(tag, message, Level.WARNING, consolePrint)
    }

    fun error(tag: String, message: String, consolePrint: Boolean = true) {
        log(tag, message, Level.SEVERE, consolePrint)
    }

    /**
     * Log message
     * If [logLevel] >= [Level.WARNING] it will be saved into local log file
     */
    fun log(tag: String, message: String, logLevel: Level, consolePrint: Boolean) {
        if (consolePrint)
            logger?.log(logLevel, "[$tag] $message")
        if (logLevel.intValue() >= Level.WARNING.intValue())
            logInFile(tag, message, logLevel)
    }


    private fun logInFile(_tag: String?, message: String, logLevel: Level) {
        val tag = "$_tag/$logLevel"
        File(logsFolderPath).apply { if (!exists()) mkdirs() }
        val path = "${logsFolderPath}${File.separator}$prefix ${getDate()}.log"
        val file = File(path)
        if (!file.exists())
            file.createNewFile()
        file.appendText("[${getTime()}] [$tag]: $message\n")
    }

    private fun getTime(): String = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())
    private fun getDate(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())

}

