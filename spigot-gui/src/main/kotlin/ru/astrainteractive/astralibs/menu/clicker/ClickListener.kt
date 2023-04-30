package ru.astrainteractive.astralibs.menu.clicker

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.menu.InventoryButton

interface ClickListener {
    fun onClick(e: InventoryClickEvent)
    fun remember(button: InventoryButton)
    fun clearClickListener()
}
