package ru.astrainteractive.astralibs.event

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin

inline fun <reified T : Event> flowEvent(
    plugin: Plugin,
    eventPriority: EventPriority = EventPriority.NORMAL
) = flowEvent(
    plugin = plugin,
    clazz = T::class.java,
    eventPriority = eventPriority
)

fun <T : Event> flowEvent(
    plugin: Plugin,
    clazz: Class<T>,
    eventPriority: EventPriority = EventPriority.NORMAL
): Flow<Event> = callbackFlow {
    val eventListener = EventListener.Default()
    val eventExecutor = EventExecutor { _, event ->
        if (event::class.java != clazz) return@EventExecutor
        trySendBlocking(event)
    }
    plugin.server.pluginManager.registerEvent(
        clazz,
        eventListener,
        eventPriority,
        eventExecutor,
        plugin
    )
    awaitClose {
        eventListener.onDisable()
    }
}
