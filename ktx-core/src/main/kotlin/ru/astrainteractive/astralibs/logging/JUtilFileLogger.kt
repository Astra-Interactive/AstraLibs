package ru.astrainteractive.astralibs.logging

import java.io.File

class JUtilFileLogger(
    override val tag: String,
    override val folder: File,
    private val logger: java.util.logging.Logger = java.util.logging.Logger.getGlobal()
) : Logger {

    override fun warning(tag: String, msg: String, logInFile: Boolean) {
        logger.warning("[$tag]: $msg")
        if (logInFile) {
            logInFile(tag, msg)
        }
    }

    override fun error(tag: String, msg: String, logInFile: Boolean) {
        logger.severe("[$tag]: $msg")
        if (logInFile) {
            logInFile(tag, msg)
        }
    }

    override fun info(tag: String, msg: String, logInFile: Boolean) {
        logger.info("[$tag]: $msg")
        if (logInFile) {
            logInFile(tag, msg)
        }
    }

    private fun logInFile(tag: String, msg: String) {
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val data = Logger.getDate()
        val time = Logger.getTime()
        val file = File(folder, "$data.log")
        if (!folder.exists()) folder.mkdirs()
        if (!file.exists()) file.createNewFile()
        file.appendText("[$time] [$tag]: $msg\n")
    }
}
