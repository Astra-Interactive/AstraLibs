package ru.astrainteractive.astralibs.event

import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * Event listener for all child bukkit events
 */
interface EventListener : Listener {
    /**
     * Register event listener so events could be delivered
     */
    fun onEnable(plugin: Plugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    /**
     * Remove event listener
     */
    fun onDisable() {
        HandlerList.unregisterAll(this)
    }
}
