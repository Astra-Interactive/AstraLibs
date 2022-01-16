package com.astrainteractive.astralibs

/**
 * Event manager for event handling.
 *
 * Every event manager SHOULD have implementation of this interface.
 *
 * It has handlers that will automatically disable
 */
interface EventManager {
    abstract val handlers: MutableList<EventListener>
    public fun addHandler(event: EventListener) {
        handlers.add(event)
    }
    public fun onDisable() {
        handlers.forEach {
            it.onDisable()
        }
    }
}