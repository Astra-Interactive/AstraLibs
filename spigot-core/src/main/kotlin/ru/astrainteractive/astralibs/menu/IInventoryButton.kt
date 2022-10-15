package ru.astrainteractive.astralibs.menu

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface IInventoryButton {
    val item: ItemStack
    val index: Int
    fun set(inventory: Inventory) {
        inventory.setItem(index, item)
    }
}