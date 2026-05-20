package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent

/** Inventory slot click handler. [Empty] is a no-op default. */
fun interface Click {
    operator fun invoke(e: InventoryClickEvent)

    object Empty : Click {
        override fun invoke(e: InventoryClickEvent) = Unit
    }
}
