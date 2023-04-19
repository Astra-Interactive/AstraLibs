package ru.astrainteractive.astralibs.filemanager

import ru.astrainteractive.astralibs.filemanager.impl.ResourceFileManagerImpl
import java.io.File

/**
 * This [ResourceFileManager] will save file from resource
 */
interface ResourceFileManager : FileManager {
    val isResourceExists: Boolean
    sealed class Exception(msg: String) : Throwable(msg) {
        class ResourceNotExists(resourceName: String) : kotlin.Exception("Resource $resourceName not exists")

    }
    companion object {
        /**
         * @param name - name of the file or name with path to file
         * @param dataFolder - folder where file will be stored
         * @param clazz - class loader where resource is located
         */
        operator fun invoke(
            name: String,
            dataFolder: File,
            clazz: Class<*>
        ): ResourceFileManager = ResourceFileManagerImpl(
            name,
            dataFolder,
            clazz
        )
    }
}