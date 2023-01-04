package ru.astrainteractive.astralibs

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

/**
 * Main instance of AstraLibs
 * You can see AstraTemplate for examples of use
 */
object AstraLibs {
    private lateinit var plugin: JavaPlugin

    /**
     * Instance of current plugin
     * @return instance of plugin
     */
    val instance: JavaPlugin
        get() = plugin

    /**
     * Initializer for AstraLibs
     */
    fun rememberPlugin(plugin: JavaPlugin) {
        AstraLibs.plugin = plugin
    }
}