package com.astrainteractive.astralibs

/**
 * Event manager for event handling.
 *
 * Every event manager SHOULD have implementation of this interface.
 *
 * It has handlers that will automatically disable
 */
interface IAstraManager {
    abstract val handlers: MutableList<IAstraListener>
    public fun addHandler(event: IAstraListener) {
        handlers.add(event)
    }
    public fun onDisable() {
        handlers.forEach {
            it.onDisable()
        }
    }
}