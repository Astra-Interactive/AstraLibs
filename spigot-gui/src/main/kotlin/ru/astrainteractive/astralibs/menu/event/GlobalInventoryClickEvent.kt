package ru.astrainteractive.astralibs.menu.event

import org.jetbrains.kotlin.tooling.core.UnsafeApi
import ru.astrainteractive.astralibs.events.EventListener

@UnsafeApi("Consider create your own Singleton InventoryClickEvent")
object GlobalInventoryClickEvent : EventListener by DefaultInventoryClickEvent()
