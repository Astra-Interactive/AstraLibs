package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/** Manages per-slot [Click] handlers for an inventory menu. */
public interface ClickListener {
    public fun onClick(e: InventoryClickEvent)

    /** Registers [click] for slot [index]. */
    public fun remember(index: Int, click: Click)

    /** Clears all registered handlers. Call before each render. */
    public fun clear()
}

/** Registers [slot]'s click handler by [InventorySlot.index]. */
public fun ClickListener.remember(slot: InventorySlot) {
    remember(slot.index, slot.click)
}
