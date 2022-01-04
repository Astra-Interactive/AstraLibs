package com.astrainteractive.astralibs

import org.bukkit.Bukkit
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level

/**
 * Logger file
 */
object Logger {
    private var prefix = "[AstraLibs]"

    /**
     * Call this method to init your plugin prefix
     */
    fun init(prefix: String) {
        Logger.prefix = "[$prefix]"
    }

    /**
     * Log message with tag
     * @see Type
     */
    fun log(message: String, tag: String? = null, consolePrint: Boolean = true) {
        log(tag, message, Type.INFO, consolePrint)
    }

    fun warn(message: String, tag: String? = null, consolePrint: Boolean = true) {
        log(tag, message, Type.WARN, consolePrint)
    }

    fun error(message: String, tag: String? = null, consolePrint: Boolean = true) {
        log(tag, message, Type.ERROR, consolePrint)
    }

    private fun log(tag: String?, message: String, type: Type, consolePrint: Boolean) {
        val tag = tag ?: "Default"
        val level = when (type) {
            Type.INFO -> Level.INFO
            Type.WARN -> Level.WARNING
            Type.ERROR -> Level.SEVERE
        }
        if (consolePrint)
            Bukkit.getLogger().log(level, "[$tag] $message")
        logInFile(tag, message, type)
    }

    private fun logInFile(_tag: String?, message: String, type: Type) {
        val tag = "$_tag/$type"
        File("${AstraLibs.instance.dataFolder}${File.separator}logs").apply { if (!exists()) mkdirs() }
        val path =
            "${AstraLibs.instance.dataFolder}${File.separator}logs${File.separator}$prefix ${getDate()}.log"
        val file = File(path)
        if (!file.exists())
            file.createNewFile()
        file.appendText("[${getTime()}] [$tag]: $message\n")
    }

    private fun getTime(): String = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())
    private fun getDate(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())

    enum class Type {
        WARN, INFO, ERROR
    }
}

