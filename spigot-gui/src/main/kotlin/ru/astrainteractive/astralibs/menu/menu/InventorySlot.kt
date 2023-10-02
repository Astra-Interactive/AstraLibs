package ru.astrainteractive.astralibs.menu.menu

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.clicker.Click

/**
 * Basic inventory button from which you can inherit
 * @see ItemStackButtonBuilder
 */
interface InventorySlot {
    val item: ItemStack
    val index: Int
    val click: Click

    class Builder private constructor() {
        var itemStack = ItemStack(Material.AIR)
        var index = 0
        var click: Click = Click.Empty

        companion object {
            operator fun invoke(block: Builder.() -> Unit): InventorySlot = object : InventorySlot {
                val builder = Builder().apply(block)
                override val item: ItemStack = builder.itemStack
                override val index: Int = builder.index
                override val click: Click = builder.click
            }
        }
    }
}
