package ru.astrainteractive.astralibs.menu.one_page

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.IInventoryButton

data class InventoryButton(override val item: ItemStack, override val index: Int = 4) : IInventoryButton {
    companion object {
        fun fromString(title: String, index: Int): InventoryButton {
            val itemStack = ItemStack(Material.PAPER).apply {
                editMeta {
                    it.setDisplayName(title)
                }
            }
            return InventoryButton(itemStack, index)
        }
    }
}