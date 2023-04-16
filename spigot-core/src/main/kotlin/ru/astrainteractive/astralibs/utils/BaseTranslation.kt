package ru.astrainteractive.astralibs.utils

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.astralibs.file_manager.SpigotFileManager
import ru.astrainteractive.astralibs.file_manager.SpigotFileManagerImpl

abstract class BaseTranslation {
    protected abstract val translationFile: SpigotFileManager
    protected val translationConfiguration: FileConfiguration
        get() = translationFile.fileConfiguration

    /**
     * This function will write non-existing translation into config file
     * So you don't need to create your config file by yourself
     * Just run plugin with this function and translation file will be generated automatically
     */
    fun translationValue(path: String, default: String): String {
        val msg = translationConfiguration.getHEXString(path) ?: default.HEX()
        if (!translationConfiguration.contains(path)) {
            translationConfiguration.set(path, default)
            translationFile.save()
        }
        return msg
    }
}