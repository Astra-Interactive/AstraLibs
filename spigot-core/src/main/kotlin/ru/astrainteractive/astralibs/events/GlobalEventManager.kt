package ru.astrainteractive.astralibs.events

/**
 * This is a global event manager for all your events
 * Don't forget to call [GlobalEventManager.onDisable] when disabling your plugin
 */
object GlobalEventManager : EventManager {
    override val handlers: MutableList<EventListener> = mutableListOf()
}
