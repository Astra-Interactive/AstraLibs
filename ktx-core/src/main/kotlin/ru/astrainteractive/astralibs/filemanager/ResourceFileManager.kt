package ru.astrainteractive.astralibs.filemanager

import ru.astrainteractive.astralibs.filemanager.impl.JVMResourceFileManager
import java.io.File

/**
 * This [ResourceFileManager] will save file from resource
 */
interface ResourceFileManager : FileManager {
    val isResourceExists: Boolean

    sealed class Exception(msg: String) : Throwable(msg) {
        class ResourceNotExists(resourceName: String) : kotlin.Exception("Resource $resourceName not exists")

    }
}