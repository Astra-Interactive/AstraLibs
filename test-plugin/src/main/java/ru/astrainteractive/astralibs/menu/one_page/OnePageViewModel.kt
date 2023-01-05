package ru.astrainteractive.astralibs.menu.one_page

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.async.AsyncComponent

class OnePageViewModel : AsyncComponent() {
    val item = MutableStateFlow<InventoryButton>(InventoryButton(ItemStack(Material.GLOWSTONE)) { onItemClicked() })
    fun onItemClicked() {
        item.update {
            val itemStack = it.item.clone().apply {
                editMeta {
                    it.setDisplayName("${ChatColor.values().random()}Hello Glowstone")
                }
            }
            it.copy(itemStack)
        }
    }
}