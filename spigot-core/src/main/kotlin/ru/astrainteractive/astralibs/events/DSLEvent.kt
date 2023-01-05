package ru.astrainteractive.astralibs.events

import ru.astrainteractive.astralibs.AstraLibs
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.java.JavaPlugin

/**
 * With this object you will no longer need to create interfaces for your bukkit event listeners
 */
object DSLEvent {
    inline fun <reified T : Event> event(
        eventManager: EventManager = GlobalEventManager,
        eventPriority: EventPriority = EventPriority.NORMAL,
        plugin: JavaPlugin = AstraLibs.instance,
        crossinline block: (T) -> Unit
    ) = event(T::class.java, eventManager, eventPriority, plugin) {
        (it as? T)?.let(block)
    }

    fun <T : Event> event(
        clazz: Class<T>,
        eventManager: EventManager,
        eventPriority: EventPriority,
        plugin: JavaPlugin,
        block: (Event) -> Unit
    ) = object : EventListener {
        override fun onEnable(manager: EventManager): EventListener {
            eventManager.addHandler(this)
            plugin.server.pluginManager.registerEvent(
                clazz, this, eventPriority,
                { _, event ->
                    block(event)
                }, plugin
            )
            return this
        }

    }.onEnable(eventManager)


}
