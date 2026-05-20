package ru.astrainteractive.astralibs.event

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.EventExecutor
import org.bukkit.plugin.Plugin

/**
 * Returns a cold [Flow] that emits every Bukkit [Event] of type [T].
 * The listener is registered on collection and unregistered on cancellation.
 */
inline fun <reified T : Event> flowEvent(
    plugin: Plugin,
    eventPriority: EventPriority = EventPriority.NORMAL
) = flowEvent(
    plugin = plugin,
    clazz = T::class.java,
    eventPriority = eventPriority
)

/**
 * Returns a cold [Flow] that emits every Bukkit [Event] of the given [clazz].
 * Subclasses are not matched; prefer the reified overload when the type is known at compile time.
 */
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
