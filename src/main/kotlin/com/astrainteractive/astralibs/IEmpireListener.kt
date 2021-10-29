package com.astrainteractive.astralibs

import org.bukkit.event.Listener
/**
 * This interface provides you comfortability whe use events
 */
interface IEmpireListener:Listener {


    fun onEnable(manager: IEventManager): IEmpireListener {
        AstraLibs.instance.server.pluginManager.registerEvents(this, AstraLibs.instance)
        manager.addHandler(this)
        return this
    }
    abstract fun onDisable()
}