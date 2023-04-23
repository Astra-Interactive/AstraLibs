package ru.astrainteractive.astralibs.menu.menu

import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.clicker.Click

/**
 * Basic inventory button from which you can inherit
 * @see ItemStackButtonBuilder
 */
interface InventoryButton {
    val item: ItemStack
    val index: Int
    val onClick: Click
}





