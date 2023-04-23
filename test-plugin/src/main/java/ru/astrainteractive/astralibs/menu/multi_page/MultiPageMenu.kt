package ru.astrainteractive.astralibs.menu.multi_page

import kotlinx.coroutines.cancel
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin
import ru.astrainteractive.astralibs.async.BukkitMainDispatcher
import ru.astrainteractive.astralibs.menu.holder.DefaultPlayerHolder
import ru.astrainteractive.astralibs.menu.holder.PlayerHolder
import ru.astrainteractive.astralibs.menu.menu.MenuSize
import ru.astrainteractive.astralibs.menu.menu.PaginatedMenu
import ru.astrainteractive.astralibs.menu.one_page.InventoryButton

class MultiPageMenu(plugin: Plugin, player: Player) : PaginatedMenu() {
    private val mainDispatcher = BukkitMainDispatcher(plugin)
    private val viewModel by lazy {
        MultiPageViewModel()

    }
    override var page: Int = 0
    override val maxItemsAmount: Int
        get() = viewModel.items.value.size
    override val prevPageButton: ru.astrainteractive.astralibs.menu.menu.InventoryButton = InventoryButton.fromString("Prev", 45){}
    override val backPageButton: ru.astrainteractive.astralibs.menu.menu.InventoryButton = InventoryButton.fromString("Back", 46){}
    override val nextPageButton: ru.astrainteractive.astralibs.menu.menu.InventoryButton = InventoryButton.fromString("Next", 47){}
    override val playerHolder: PlayerHolder = DefaultPlayerHolder(player)
    override var menuTitle: String = "Title Page: $page"
        get() = "Title Page: $page"
        set(value) {
            field = value
        }
    override val menuSize: MenuSize
        get() = MenuSize.XL

    override fun onInventoryClicked(e: InventoryClickEvent) {
        e.isCancelled = true
        if (e.slot < 45)
            viewModel.onClicked()
    }

    override fun onInventoryClose(it: InventoryCloseEvent) {
        viewModel.close()
    }

    override fun onPageChanged() {
        inventory.clear()
        setItems(viewModel.items.value)
    }

    override fun onCreated() {
        viewModel.items.collectOn(mainDispatcher) {
            setItems(it)
        }
    }

    override fun close() {
        super.close()
        mainDispatcher.cancel()
    }

    private fun setItems(items: List<ItemStack>) {
//        setManageButtons()
        for (i in 0 until maxItemsPerPage) {
            val index = getIndex(i)
            val item = items.getOrNull(index) ?: continue
            inventory.setItem(i, item)
        }
    }
}