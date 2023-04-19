package ru.astrainteractive.astralibs.filemanager.impl

import ru.astrainteractive.astralibs.filemanager.FileManager
import java.io.File

internal class FileManagerImpl(
    override val name: String,
    override val dataFolder: File,
) : FileManager {
    override val configFile: File
        get() = loadConfigFile()

    private fun loadConfigFile(): File {
        val file = File(dataFolder, name)
        if (file.exists()) return file

        File(dataFolder, name)
        file.parentFile?.mkdirs()
        file.createNewFile()
        return file
    }

    override fun delete() {
        if (configFile.exists())
            configFile.delete()
    }

}