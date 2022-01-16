package com.astrainteractive.astralibs

import org.bukkit.event.Listener
/**
 * This interface provides you comfortability whe use events
 */
interface EventListener:Listener {


    fun onEnable(manager: EventManager): EventListener {
        AstraLibs.instance.server.pluginManager.registerEvents(this, AstraLibs.instance)
        manager.addHandler(this)
        return this
    }
    abstract fun onDisable()
}