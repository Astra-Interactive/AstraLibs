package ru.astrainteractive.astralibs.logging

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface Logger {
    val tag: String
    val folder: File
    fun warning(tag: String, msg: String, logInFile: Boolean = false)
    fun error(tag: String, msg: String, logInFile: Boolean = false)
    fun info(tag: String, msg: String, logInFile: Boolean = false)

    companion object {
        /**
         * Returns current time in HH:mm:ss format
         */
        fun getTime(): String = DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())

        /**
         * Returns current date in yyyy-MM-dd format
         */
        fun getDate(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now())

    }
}