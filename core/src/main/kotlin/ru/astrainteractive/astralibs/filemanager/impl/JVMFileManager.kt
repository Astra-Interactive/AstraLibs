package ru.astrainteractive.astralibs.filemanager.impl

import ru.astrainteractive.astralibs.filemanager.FileManager
import java.io.File

/**
 * @param name - name of the file or name with path to file
 * @param dataFolder - folder where file will be stored
 */
class JVMFileManager(
    override val name: String,
    override val dataFolder: File,
) : FileManager {
    private val file: File
        get() = File(dataFolder, name)

    override val configFile: File
        get() {
            if (file.exists()) return file
            file.parentFile?.mkdirs()
            file.createNewFile()
            return file
        }

    override fun delete() {
        if (!configFile.exists()) return
        configFile.delete()
    }
}
