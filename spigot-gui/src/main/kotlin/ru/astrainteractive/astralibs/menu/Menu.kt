package ru.astrainteractive.astralibs.menu

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import ru.astrainteractive.astralibs.async.BukkitMain
import ru.astrainteractive.astralibs.events.DSLEvent
import ru.astrainteractive.astralibs.events.EventListener
import ru.astrainteractive.astralibs.events.EventManager
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.async.AsyncComponent
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
                withContext(Dispatchers.BukkitMain) {
                    block(it)
                }
            }
        }
    }

    fun IInventoryButton.setInventoryButton() {
        inventory?.setItem(index, item)
    }

    abstract val playerMenuUtility: IPlayerHolder


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
     */
    abstract fun onInventoryClose(it: InventoryCloseEvent)

    /**
     * Open inventory method for Menu class
     */
    suspend fun open() {
        inventory = Bukkit.createInventory(this, menuSize.size, menuTitle)
        withContext(Dispatchers.BukkitMain) {
            inventory?.let(playerMenuUtility.player::openInventory)
            onCreated()
        }
    }


    abstract fun onCreated()
}
