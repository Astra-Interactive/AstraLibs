package ru.astrainteractive.astralibs.menu.menu

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.async.AsyncComponent
import ru.astrainteractive.astralibs.menu.holder.PlayerHolder

/**
 * Default menu abstract class
 * Don't forget to add [MenuListener]
 */
@SuppressWarnings("Don't forget to add MenuListener")
abstract class Menu : InventoryHolder, AsyncComponent() {

    fun <T> StateFlow<T>.collectOn(scope: CoroutineDispatcher, block: (T) -> Unit) {
        componentScope.launch(scope) {
            collectLatest {
                block(it)
            }
        }
    }

    fun InventorySlot.setInventorySlot() {
        inventory?.setItem(index, item)
    }

    abstract val playerHolder: PlayerHolder

    private var inventory: Inventory? = null

    override fun getInventory(): Inventory = checkNotNull(inventory) { "Inventory not initialized" }

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
     *
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

    /**
     * This method called after inventory created and opened
     */
    abstract fun onCreated()
}
