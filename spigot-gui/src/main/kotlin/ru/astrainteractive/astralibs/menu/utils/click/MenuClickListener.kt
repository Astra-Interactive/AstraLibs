package ru.astrainteractive.astralibs.menu.utils.click

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.menu.Menu
import ru.astrainteractive.astralibs.menu.utils.InventoryButton

/**
 * This click listener listens for top inventory which is [Menu] to be clicked
 */
class MenuClickListener : ClickListener {
    private val clicksMap = HashMap<Int, Click>()
    override fun onClick(e: InventoryClickEvent) {
        if (e.clickedInventory?.holder !is Menu) return
        clicksMap[e.slot]?.invoke(e)
    }

    override fun remember(button: InventoryButton) {
        clicksMap[button.index] = button.onClick
    }

    override fun clearClickListener() {
        clicksMap.clear()
    }

}