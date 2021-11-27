package com.astrainteractive.astralibs

import org.bukkit.ChatColor
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Logger file
 */
object Logger {
    private var prefix = "[AstraLibs]"

    /**
     * Call this method to init your plugin prefix
     */
    fun init(prefix: String) {
        this.prefix = "[$prefix]"
    }

    /**
     * Log message with tag
     * @see Type
     */
    fun log(tag: String, vararg message: String, logType: Type = Type.LOG, logInFile: Boolean = true) {
        log(tag, message.joinToString(" "), logType, logInFile)
    }

    private fun log(tag: String, message: String, logType: Type = Type.LOG, logInFile: Boolean = true) {
        val log = "[$tag]: $message"
        if (logInFile)
            File("${AstraLibs.instance.dataFolder}${File.separator}${prefix}_${getDate()}.log").appendText("[${getTime()}]: $log\n")
        println("${logType.color}${prefix} $log")
    }

    private fun getTime():String = DateTimeFormatter.ofPattern("HH-mm-ss").format(LocalDateTime.now())
    private fun getDate(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())

    enum class Type(val color: ChatColor) {
        WARN(ChatColor.YELLOW), ERROR(ChatColor.RED), LOG(ChatColor.AQUA)
    }
}

