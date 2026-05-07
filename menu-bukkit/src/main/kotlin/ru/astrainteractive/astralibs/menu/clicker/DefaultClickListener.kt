package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.core.Menu
import ru.astrainteractive.astralibs.menu.inventory.api.InventoryMenu

/**
 * This click listener listens for top inventory which is [InventoryMenu] to be clicked
 */
class DefaultClickListener : ClickListener {
    private val clicksMap = HashMap<Int, Click>()

    override fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory?.holder !is Menu) return
        clicksMap[e.slot]?.invoke(e)
    }

    override fun remember(index: Int, click: Click) {
        clicksMap[index] = click
    }

    override fun clear() {
        clicksMap.clear()
    }
}
