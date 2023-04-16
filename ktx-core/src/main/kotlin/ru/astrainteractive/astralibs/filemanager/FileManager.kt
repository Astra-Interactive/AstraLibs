package ru.astrainteractive.astralibs.filemanager

import java.io.File

interface FileManager {
    val name: String

    val dataFolder: File

    val configFile: File

    val isResourceExists: Boolean
    fun loadFromResource(): File
    fun loadConfigFile(): File
    fun save()
    fun reload()
    sealed class Exception(msg: String) : Throwable(msg) {
        class ResourceNotExists(resourceName: String) : kotlin.Exception("Resource $resourceName not exists")

    }
}

