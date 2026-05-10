package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/**
 * Manages per-slot [Click] handlers for an inventory menu.
 */
interface ClickListener {
    /** Dispatches the event to the handler registered for the clicked slot. */
    fun onClick(e: InventoryClickEvent)

    /** Registers [click] for slot [index]. */
    fun remember(index: Int, click: Click)

    /** Clears all registered handlers. Call before each render. */
    fun clear()
}

/** Registers [slot]'s click handler by [InventorySlot.index]. */
fun ClickListener.remember(slot: InventorySlot) {
    remember(slot.index, slot.click)
}
