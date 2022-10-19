package ru.astrainteractive.astralibs.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

interface IInventoryButton {
    val item: ItemStack
    val index: Int
    val onClick: (e: InventoryClickEvent) -> Unit
}

fun IInventoryButton.set(inventory: Inventory) {
    inventory.setItem(index, item)
}