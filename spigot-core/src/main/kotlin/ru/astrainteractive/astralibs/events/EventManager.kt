package ru.astrainteractive.astralibs.events

import org.bukkit.event.HandlerList

/**
 * Event manager for event handling.
 *
 * Every event manager SHOULD have implementation of this interface.
 *
 * It has handlers that will automatically disable
 */
interface EventManager {
    val handlers: MutableList<EventListener>
    fun addHandler(event: EventListener) {
        handlers.add(event)
    }
    fun onDisable() {
        handlers.forEach {
            it.onDisable()
        }
    }
}