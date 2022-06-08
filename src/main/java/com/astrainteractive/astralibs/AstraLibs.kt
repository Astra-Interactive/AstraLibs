package com.astrainteractive.astralibs

import com.astrainteractive.astralibs.async.AsyncHelper
import com.astrainteractive.astralibs.observer.LifecycleOwner
import kotlinx.coroutines.cancel
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

/**
 * Main instance of AstraLibs
 * You can see AstraTemplate for examples of use
 */
object AstraLibs : LifecycleOwner {
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