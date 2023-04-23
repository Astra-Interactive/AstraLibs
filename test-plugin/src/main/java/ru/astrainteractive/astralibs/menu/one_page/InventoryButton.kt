package ru.astrainteractive.astralibs.menu.one_page

import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.menu.InventoryButton
import ru.astrainteractive.astralibs.menu.clicker.Click

data class InventoryButton(
    override val item: ItemStack,
    override val index: Int = 4,
    override val onClick: Click
) : InventoryButton {
    companion object {
        fun fromString(title: String, index: Int, onClick: (e: InventoryClickEvent) -> Unit): InventoryButton {
            val itemStack = ItemStack(Material.PAPER).apply {
                editMeta {
                    it.setDisplayName(title)
                }
            }
            return InventoryButton(itemStack, index, onClick)
        }
    }

}