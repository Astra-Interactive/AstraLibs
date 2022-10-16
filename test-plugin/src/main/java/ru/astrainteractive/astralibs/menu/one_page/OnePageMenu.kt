package ru.astrainteractive.astralibs.menu.one_page

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import ru.astrainteractive.astralibs.async.BukkitMain
import ru.astrainteractive.astralibs.menu.AstraMenuSize
import ru.astrainteractive.astralibs.menu.DefaultPlayerHolder
import ru.astrainteractive.astralibs.menu.IPlayerHolder
import ru.astrainteractive.astralibs.menu.Menu
import ru.astrainteractive.astralibs.menu.multi_page.MultiPageMenu

class OnePageMenu(player: Player) : Menu(), IInventoryView {
    override val playerMenuUtility: IPlayerHolder = DefaultPlayerHolder(player)
    val viewModel: OnePageViewModel by lazy {
        OnePageViewModel()
    }
    val presenter: OnePagePresenter by lazy {
        OnePagePresenter(this)
    }
    override var menuTitle: String = "One page menu"
    override val menuSize: AstraMenuSize
        get() = AstraMenuSize.M

    override fun onInventoryClicked(e: InventoryClickEvent) {
        e.isCancelled = true
//        if (e.slot == viewModel.item.value.index)
//            viewModel.onItemClicked()
//        if (e.slot == viewModel.item.value.index)
//            presenter.onItemClicked()
        if (e.slot == viewModel.item.value.index)
            lifecycleScope.launch(Dispatchers.IO) {
                MultiPageMenu(playerMenuUtility.player).open()
            }
    }

    override fun onInventoryClose(it: InventoryCloseEvent) {
        viewModel.clear()
        presenter.clear()
    }

    override fun onCreated() {
        presenter.onBinded()
//        viewModel.item.collectOn {
//            showInventoryButton(it)
//        }
    }

    override fun showInventoryButton(inventoryButton: InventoryButton) {
        inventoryButton.setInventoryButton()
    }


}