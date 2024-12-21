package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.inventory.InventoryMenu
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/**
 * This click listener listens for top inventory which is [InventoryMenu] to be clicked
 */
class MenuClickListener : ClickListener {
    private val clicksMap = HashMap<Int, Click>()

    override fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory?.holder !is InventoryMenu) return
        clicksMap[e.slot]?.invoke(e)
    }

    override fun remember(button: InventorySlot) {
        clicksMap[button.index] = button.click
    }

    override fun clear() {
        clicksMap.clear()
    }
}
