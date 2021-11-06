package com.astrainteractive.astralibs

import org.bukkit.event.Listener
/**
 * This interface provides you comfortability whe use events
 */
interface IAstraListener:Listener {


    fun onEnable(manager: IAstraManager): IAstraListener {
        AstraLibs.instance.server.pluginManager.registerEvents(this, AstraLibs.instance)
        manager.addHandler(this)
        return this
    }
    abstract fun onDisable()
}