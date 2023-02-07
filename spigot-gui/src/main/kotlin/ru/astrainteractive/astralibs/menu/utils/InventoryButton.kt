package ru.astrainteractive.astralibs.menu.utils

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.menu.Menu
import ru.astrainteractive.astralibs.menu.utils.click.Click

/**
 * Basic inventory button from which you can inherit
 * @see ItemStackButtonBuilder
 */
interface InventoryButton {
    val item: ItemStack
    val index: Int
    val onClick: Click
}





