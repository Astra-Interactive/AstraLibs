package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent

/** Inventory slot click handler. [Empty] is a no-op default. */
public fun interface Click {
    public operator fun invoke(e: InventoryClickEvent)

    public object Empty : Click {
        override fun invoke(e: InventoryClickEvent): Unit = Unit
    }
}
