package ru.astrainteractive.astralibs.file_manager

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.filemanager.FileManager
import java.io.File

interface SpigotFileManager : FileManager {
    val fileConfiguration: FileConfiguration
    fun loadFileConfiguration(): FileConfiguration

    companion object {
        operator fun invoke(
            name: String,
            dataFolder: File = AstraLibs.instance.dataFolder,
        ): SpigotFileManager = SpigotFileManagerImpl(
            name = name,
            dataFolder = dataFolder
        )
    }
}