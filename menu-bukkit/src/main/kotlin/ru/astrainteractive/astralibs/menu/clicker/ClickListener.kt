package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.inventory.api.InventoryMenu
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/**
 * Click listener for [InventoryMenu]
 */
interface ClickListener {
    /**
     * Place this in [InventoryMenu.onInventoryClickEvent]
     */
    fun onClick(e: InventoryClickEvent)

    fun remember(index: Int, click: Click)

    /**
     * Place this in re-render of menu content
     */
    fun clear()
}

/**
 * Place this on [InventorySlot] creation
 */
fun ClickListener.remember(slot: InventorySlot) {
    remember(slot.index, slot.click)
}
