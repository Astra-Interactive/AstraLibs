package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.menu.InventorySlot
import ru.astrainteractive.astralibs.menu.menu.Menu

/**
 * This click listener listens for top inventory which is [Menu] to be clicked
 */
class MenuClickListener : ClickListener {
    private val clicksMap = HashMap<Int, Click>()
    override fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory?.holder !is Menu) return
        clicksMap[e.slot]?.invoke(e)
    }

    override fun remember(button: InventorySlot) {
        clicksMap[button.index] = button.click
    }

    override fun clear() {
        clicksMap.clear()
    }
}
