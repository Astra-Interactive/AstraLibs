package ru.astrainteractive.astralibs.menu

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

fun IPlayerHolder.hold(player: Player) = object : IPlayerHolder {
    override val player: Player = player
}

fun IInventoryButton.button(
    material: Material,
    index: Int,
    onClick: (e: InventoryClickEvent) -> Unit = {},
    metaBuilder: ItemMeta.() -> Unit = {}
) = object : IInventoryButton {
    override val item: ItemStack = ItemStack(material).apply {
        editMeta(metaBuilder)
    }
    override val index: Int = index
    override val onClick: (e: InventoryClickEvent) -> Unit = onClick

}