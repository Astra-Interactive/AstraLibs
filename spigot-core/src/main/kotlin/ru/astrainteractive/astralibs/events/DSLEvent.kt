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
        eventController: EventListener = GlobalEventListener,
        eventPriority: EventPriority = EventPriority.NORMAL,
        plugin: JavaPlugin = AstraLibs.instance,
        crossinline block: (T) -> Unit
    ) = event(T::class.java, eventController, eventPriority, plugin) {
        (it as? T)?.let(block)
    }

    fun <T : Event> event(
        clazz: Class<T>,
        eventController: EventListener,
        eventPriority: EventPriority,
        plugin: JavaPlugin,
        block: (Event) -> Unit
    ) = plugin.server.pluginManager.registerEvent(
        clazz, eventController, eventPriority,
        { _, event ->
            block(event)
        }, plugin
    )


}
