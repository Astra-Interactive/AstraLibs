package com.astrainteractive.astralibs

import com.astrainteractive.astralibs.utils.catching
import org.bukkit.Bukkit
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.logging.Level

/**
 * Logger file
 */
object Logger {
    var prefix = "[AstraLibs]"
        set(value) {
            field = "[$value]"
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

        if (consolePrint)
            catching { Bukkit.getLogger().log(type.level, "[$tag] $message") }?: println("[$tag] $message")
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

    enum class Type(val level: Level) {
        WARN(Level.WARNING), INFO(Level.INFO), ERROR(Level.SEVERE)
    }
}

