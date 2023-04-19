package ru.astrainteractive.astralibs.filemanager

import ru.astrainteractive.astralibs.filemanager.impl.FileManagerImpl
import java.io.File

/**
 * This class will help manage files
 * It will auto-create it inside [dataFolder] based on [name]
 */
interface FileManager {
    /**
     * Name of the file
     * Example: file.json; folder/nextfolder/file.json
     */
    val name: String

    /**
     * Path to data folder of the plugin
     */
    val dataFolder: File

    /**
     * Configuration file named [name] placed in [dataFolder]
     */
    val configFile: File

    /**
     * Delete [configFile]
     */
    fun delete()


    companion object {
        /**
         * @param name - name of the file or name with path to file
         * @param dataFolder - folder where file will be stored
         */
        operator fun invoke(
            name: String,
            dataFolder: File,
        ): FileManager = FileManagerImpl(name, dataFolder)
    }
}

