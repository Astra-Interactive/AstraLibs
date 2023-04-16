package ru.astrainteractive.astralibs.filemanager

import java.io.File

interface FileManager {
    val name: String

    val dataFolder: File

    val configFile: File

    val isResourceExists: Boolean
    fun loadFromResource(fileName: String): File
    fun loadConfigFile(): File
    fun save()
    fun reload()
}

