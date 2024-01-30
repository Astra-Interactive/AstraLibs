package ru.astrainteractive.astralibs.menu.slot

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.clicker.Click
import ru.astrainteractive.astralibs.menu.slot.util.InventorySlotExt

/**
 * Basic inventory button from which you can inherit
 * @see InventorySlotExt
 */
interface InventorySlot {
    val item: ItemStack
    val index: Int
    val click: Click

    class Builder {
        var itemStack = ItemStack(Material.AIR)
        var index = 0
        var click: Click = Click.Empty

        fun build(): InventorySlot = object : InventorySlot {
            override val item: ItemStack = this@Builder.itemStack
            override val index: Int = this@Builder.index
            override val click: Click = this@Builder.click
        }
    }
}
