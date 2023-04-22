package ru.astrainteractive.astralibs

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.di.Singleton

/**
 * Main instance of AstraLibs
 * You can see AstraTemplate for examples of use
 */
object AstraLibs: Singleton<JavaPlugin>() {
    /**
     * Initializer for AstraLibs
     */
    fun rememberPlugin(plugin: JavaPlugin) {
        this.instance = plugin
    }
}