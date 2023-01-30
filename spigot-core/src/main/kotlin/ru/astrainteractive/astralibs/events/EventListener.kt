package ru.astrainteractive.astralibs.events

import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin

interface EventListener : Listener {
    fun onEnable(plugin: Plugin) {
        plugin.server.pluginManager.registerEvents(this, plugin)
    }

    fun onDisable() {
        HandlerList.unregisterAll(this)
    }
}


