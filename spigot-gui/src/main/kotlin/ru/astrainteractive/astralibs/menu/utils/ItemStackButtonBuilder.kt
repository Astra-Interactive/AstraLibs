package ru.astrainteractive.astralibs.menu.utils

import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.utils.click.Click

/**
 * Default builder for [InventoryButton]
 */
class ItemStackButtonBuilder {
    var itemStack = ItemStack(Material.AIR)
    var index = 0
    var onClick: (e: InventoryClickEvent) -> Unit = { }
    fun itemStack(block: ItemStack.() -> Unit) = itemStack.apply(block)
    companion object {
        operator fun invoke(block: ItemStackButtonBuilder.() -> Unit): InventoryButton = object : InventoryButton {
            val builder = ItemStackButtonBuilder().apply(block)
            override val item: ItemStack = builder.itemStack
            override val index: Int = builder.index
            override val onClick: Click = Click { builder.onClick(it) }

        }
    }
}