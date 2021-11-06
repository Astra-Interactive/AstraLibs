package com.astrainteractive.astralibs

import org.bukkit.ChatColor
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {
    private var prefix = "[AstraLibs]"
    fun init(prefix: String) {
        this.prefix = "[$prefix]"
    }

    fun log(tag: String, vararg message: String, logType: Type = Type.LOG, logInFile: Boolean = false) {
        log(tag, message.joinToString(" "), logType, logInFile)
    }

    private fun log(tag: String, message: String, logType: Type = Type.LOG, logInFile: Boolean = false) {
        val log = "$tag: $message"
        if (logInFile)
            File("${AstraLibs.instance.dataFolder}${File.separator}${prefix}_${getDate()}.log").appendText("$log\n")
        println("${logType.color}${prefix} $log")
    }

    private fun getDate(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())

    enum class Type(val color: ChatColor) {
        WARN(ChatColor.YELLOW), ERROR(ChatColor.RED), LOG(ChatColor.AQUA)
    }
}

