package ru.astrainteractive.astralibs.menu.multi_page

import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.*
import ru.astrainteractive.astralibs.menu.one_page.InventoryButton

class MultiPageMenu(player: Player) : PaginatedMenu() {
    private val viewModel by lazy {
        MultiPageViewModel()
    }
    override var page: Int = 0
    override val maxItemsAmount: Int
        get() = viewModel.items.value.size
    override val prevPageButton: IInventoryButton = InventoryButton.fromString("Prev", 45){}
    override val backPageButton: IInventoryButton = InventoryButton.fromString("Back", 46){}
    override val nextPageButton: IInventoryButton = InventoryButton.fromString("Next", 47){}
    override val playerMenuUtility: IPlayerHolder = PlayerHolder(player)
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
        viewModel.items.collectOn {
            setItems(it)
        }
    }

    private fun setItems(items: List<ItemStack>) {
        setManageButtons()
        for (i in 0 until maxItemsPerPage) {
            val index = getIndex(i)
            val item = items.getOrNull(index) ?: continue
            inventory.setItem(i, item)
        }
    }
}