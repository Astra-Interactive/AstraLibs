package ru.astrainteractive.astralibs.menu.menu

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import ru.astrainteractive.astralibs.async.BukkitMain
import org.bukkit.Bukkit
import org.bukkit.Warning
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.async.AsyncComponent
import ru.astrainteractive.astralibs.menu.holder.PlayerHolder
import ru.astrainteractive.astralibs.menu.utils.InventoryButton
import ru.astrainteractive.astralibs.menu.utils.MenuSize
import java.lang.IllegalStateException


/**
 * Default menu abstract class
 * Don't forget to add [MenuListener]
 */
@SuppressWarnings("Don't forget to add MenuListener")
abstract class Menu : InventoryHolder, AsyncComponent() {

    fun <T> StateFlow<T>.collectOn(scope: CoroutineDispatcher = Dispatchers.BukkitMain, block: (T) -> Unit) {
        componentScope.launch(scope) {
            collectLatest {
                block(it)
            }
        }
    }

    fun InventoryButton.setInventoryButton() {
        inventory?.setItem(index, item)
    }

    abstract val playerHolder: PlayerHolder


    private var inventory: Inventory? = null
    override fun getInventory(): Inventory = inventory ?: throw IllegalStateException("Inventory not initialized")

    /**
     * Title of this inventory
     */
    abstract var menuTitle: String

    /**
     * Size of inventory
     */
    abstract val menuSize: MenuSize

    /**
     * Menu handler
     */
    abstract fun onInventoryClicked(e: InventoryClickEvent)

    /**
     * Called when inventory was closed
     * After [onInventoryClose] executed - [componentScope] will be closed
     */
    abstract fun onInventoryClose(it: InventoryCloseEvent)

    /**
     * Open inventory method for Menu class
     * Should be executed on main thread
     */
    fun open() {
        inventory = Bukkit.createInventory(this, menuSize.size, menuTitle)
        inventory?.let(playerHolder.player::openInventory)
        onCreated()
    }


    abstract fun onCreated()
}