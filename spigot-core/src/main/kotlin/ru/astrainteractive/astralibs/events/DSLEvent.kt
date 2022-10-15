package ru.astrainteractive.astralibs.events

import ru.astrainteractive.astralibs.AstraLibs
import ru.astrainteractive.astralibs.utils.catching
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

/**
 * TODO get rid of reified
 * With this object you will no longer need to create interfaces for your bukkit event listeners
 */
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
