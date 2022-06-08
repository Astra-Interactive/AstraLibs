package com.astrainteractive.astralibs.events

import com.astrainteractive.astralibs.AstraLibs
import com.astrainteractive.astralibs.catching
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

object DSLEvent{
    inline fun <reified T : Event> event(
        clazz: Class<T>,
        eventManager: EventManager = GlobalEventManager,
        eventPriority: EventPriority = EventPriority.NORMAL,
        plugin:JavaPlugin = AstraLibs.instance,
        crossinline block: (T) -> Unit
    ) =
        object : EventListener {
            override fun onEnable(manager: EventManager): EventListener {
                eventManager.addHandler(this)
                plugin.server.pluginManager.registerEvent(
                    clazz, this, eventPriority,
                    { _, event ->
                        catching { event as T }?.let(block)
                    }, plugin
                )
                return this
            }

            override fun onDisable() {
                HandlerList.unregisterAll(this)
            }

        }.onEnable(eventManager)


}
object GlobalEventManager : EventManager {
    override val handlers: MutableList<EventListener> = mutableListOf()
}
