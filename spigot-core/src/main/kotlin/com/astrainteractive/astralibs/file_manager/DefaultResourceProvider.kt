package com.astrainteractive.astralibs.file_manager

import com.astrainteractive.astralibs.AstraLibs
import java.io.File

class DefaultResourceProvider(private val dataFolder: File) : IResourceProvider {
    override fun isResourceExists(fileName: String): Boolean = AstraLibs.instance.getResource(fileName) != null

    override fun saveFromResource(fileName: String): File {
        AstraLibs.instance.saveResource(fileName, false)
        return File(dataFolder, fileName)
    }

}