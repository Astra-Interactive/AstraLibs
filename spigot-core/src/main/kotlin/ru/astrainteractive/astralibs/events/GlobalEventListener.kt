package ru.astrainteractive.astralibs.events

import org.jetbrains.kotlin.tooling.core.UnsafeApi

/**
 * This is a global event manager for all your events
 * Don't forget to call [GlobalEventListener.onDisable] when disabling your plugin
 */
@UnsafeApi("Consider create your own Singleton EventListener")
object GlobalEventListener : EventListener
