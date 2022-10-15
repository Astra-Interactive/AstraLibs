package ru.astrainteractive.astralibs.menu.one_page

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.architecture.Presenter

class OnePagePresenter(override val viewState: IInventoryView) : Presenter<IInventoryView>() {
    private var item = InventoryButton(ItemStack(Material.GLOWSTONE))
    fun onItemClicked() {
        val itemStack = item.item.clone().apply {
            editMeta {
                it.setDisplayName("${ChatColor.values().random()}Hello Glowstone")
            }
        }
        item = item.copy(itemStack)
        viewState.showInventoryButton(item)
    }

    override fun onBinded() {
        viewState.showInventoryButton(item)
    }
}