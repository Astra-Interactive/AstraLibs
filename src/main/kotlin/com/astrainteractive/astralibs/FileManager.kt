package com.astrainteractive.astralibs

import com.astrainteractive.astralibs.AstraLibs
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.lang.IllegalArgumentException

/**
 * File manager for every single file
 * You can create new files, change them, save/load them
 * If file not exist in resouces, it will be created anyway
 * @param configName is name of the file with file type
 */
public class FileManager(var configName: String) {


    /**
     * Reference for the file
     */
    private var configFile: File? = null

    /**
     * Reference for file configuration
     */
    private var dataConfig: FileConfiguration? = null

    /**
     * Initialization of file
     */
    init {
        saveDefaultConfig()
    }


    private fun reloadConfig() {
        if (this.configFile == null) this.configFile = File(AstraLibs.instance.dataFolder, configName)
        dataConfig = YamlConfiguration.loadConfiguration(configFile!!)
        val defaultConfig = YamlConfiguration.loadConfiguration(this.configFile!!)
        this.dataConfig?.setDefaults(defaultConfig)

    }

    fun getName(): String {
        return configName
    }


    fun getConfig(): FileConfiguration {
        if (this.dataConfig == null) reloadConfig()
        return this.dataConfig!!
    }

    fun getFile(): File {
        if (this.dataConfig == null) reloadConfig()
        return this.configFile!!
    }


    fun updateConfig(conf: FileConfiguration) {
        this.dataConfig = conf
    }


    /**
     * function allows you to save dataConfig
     */
    fun saveConfig() {
        if (this.configFile == null || this.dataConfig == null) return
        try {
            getConfig().save(this.configFile!!)
        } catch (e: IOException) {
            println("${ChatColor.RESET}Error during saving $configName")
        }
    }

    /**
     * Initialization of file
     */
    private fun saveDefaultConfig() {
        if (this.configFile == null) this.configFile = File(AstraLibs.instance.dataFolder, configName)
        try {
            if (this.configFile?.exists() != true) AstraLibs.instance.saveResource(configName, false)
        } catch (e: IllegalArgumentException) {
            this.configFile = File(AstraLibs.instance.dataFolder, configName)
            this.configFile?.parentFile?.mkdirs()
            this.configFile?.createNewFile()
            println("${ChatColor.YELLOW} Non standart file: $configName")
        }
    }
}