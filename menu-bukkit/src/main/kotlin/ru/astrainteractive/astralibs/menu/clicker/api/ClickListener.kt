package ru.astrainteractive.astralibs.menu.clicker.api

import org.bukkit.event.inventory.InventoryClickEvent

/** Manages per-slot [Click] handlers for an inventory menu. */
public interface ClickListener {
    public fun onClick(e: InventoryClickEvent)

    /** Registers [click] for slot [index]. */
    public fun remember(index: Int, click: Click)

    /** Clears all registered handlers. Call before each render. */
    public fun clear()
}
