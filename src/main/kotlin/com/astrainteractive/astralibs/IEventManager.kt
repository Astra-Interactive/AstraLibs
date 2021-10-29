package com.astrainteractive.astralibs

/**
 * Event manager for event handling.
 *
 * Every event manager SHOULD have implementation of this interface.
 *
 * It has handlers that will automatically disable
 */
interface IEventManager {
    abstract val handlers: MutableList<IEmpireListener>
    public fun addHandler(event: IEmpireListener) {
        handlers.add(event)
    }
    public fun onDisable() {
        handlers.forEach {
            it.onDisable()
        }
    }
}