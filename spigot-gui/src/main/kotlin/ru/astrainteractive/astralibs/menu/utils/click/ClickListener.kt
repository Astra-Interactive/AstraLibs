package ru.astrainteractive.astralibs.menu.utils.click

import org.bukkit.event.inventory.InventoryClickEvent
import ru.astrainteractive.astralibs.menu.utils.InventoryButton

interface ClickListener {
    fun onClick(e: InventoryClickEvent)
    fun remember(button: InventoryButton)
    fun clearClickListener()
}