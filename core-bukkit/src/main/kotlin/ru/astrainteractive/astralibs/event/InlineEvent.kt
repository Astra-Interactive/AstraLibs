package ru.astrainteractive.astralibs.event

import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.plugin.Plugin

@Deprecated("Use flow-based events")
inline fun <reified T : Event> inlineEvent(
    eventListener: EventListener,
    plugin: Plugin,
    eventPriority: EventPriority = EventPriority.NORMAL,
    crossinline block: (T) -> Unit
) = inlineEvent(T::class.java, eventListener, plugin, eventPriority) { event ->
    (event as? T)?.let(block)
}

@Deprecated("Use flow-based events")
fun <T : Event> inlineEvent(
    clazz: Class<T>,
    eventListener: EventListener,
    plugin: Plugin,
    eventPriority: EventPriority,
    block: (Event) -> Unit
) = plugin.server.pluginManager.registerEvent(
    clazz,
    eventListener,
    eventPriority,
    { _, event -> block.invoke(event) },
    plugin
)
