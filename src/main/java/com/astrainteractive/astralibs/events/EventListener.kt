package com.astrainteractive.astralibs.events

import com.astrainteractive.astralibs.AstraLibs
import org.bukkit.event.HandlerList
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

    /**
     * You should unregister all the listeners here
     */
    fun onDisable(){
        HandlerList.unregisterAll(this)
    }
}