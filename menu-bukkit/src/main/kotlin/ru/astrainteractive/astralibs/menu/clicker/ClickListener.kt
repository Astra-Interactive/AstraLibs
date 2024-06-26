package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.inventory.InventoryMenu
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/**
 * Click listener for [InventoryMenu]
 */
interface ClickListener {
    /**
     * Place this in [InventoryMenu.onInventoryClicked]
     */
    fun onClick(e: InventoryClickEvent)

    /**
     * Place this on [InventorySlot] creation
     */
    fun remember(button: InventorySlot)

    /**
     * Place this in re-render of menu content
     */
    fun clear()
}
