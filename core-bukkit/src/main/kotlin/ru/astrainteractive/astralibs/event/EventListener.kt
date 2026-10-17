package ru.astrainteractive.astralibs.event

import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

/**
 * Event listener for all child bukkit events
 */
public interface EventListener : Listener {
    /**
     * Register event listener so events could be delivered
     */
    public fun onEnable(plugin: Plugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    /**
     * Remove event listener
     */
    public fun onDisable() {
        HandlerList.unregisterAll(this)
    }

    /** Concrete no-op implementation of [EventListener] used as a registration anchor. */
    public class Default : EventListener
}
