package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.menu.InventorySlot
import ru.astrainteractive.astralibs.menu.menu.Menu

/**
 * Click listener for [Menu]
 */
interface ClickListener {
    /**
     * Place this in [Menu.onInventoryClicked]
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
