package ru.astrainteractive.astralibs.menu

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import ru.astrainteractive.astralibs.async.BukkitMain
import ru.astrainteractive.astralibs.events.DSLEvent
import ru.astrainteractive.astralibs.events.EventListener
import ru.astrainteractive.astralibs.events.EventManager
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.architecture.AsyncComponent


/**
 * Default menu abstract class
 * Don't forget to add [MenuListener]
 */
@SuppressWarnings("Don't forget to add MenuListener")
abstract class Menu : InventoryHolder, AsyncComponent() {
    val lifecycleScope: CoroutineScope
        get() = scope

    fun <T> StateFlow<T>.collectOn(scope: CoroutineDispatcher = Dispatchers.Main, block: (T) -> Unit) {
        lifecycleScope.launch(Dispatchers.IO) {
            collectLatest {
                withContext(Dispatchers.BukkitMain) {
                    block(it)
                }
            }
        }
    }

    object InventoryEventHandler : EventManager {
        override val handlers: MutableList<EventListener> = mutableListOf()
    }

    fun IInventoryButton.setInventoryButton() {
        inventory?.setItem(index, item)
    }

    val onClickDetector = DSLEvent.event(InventoryClickEvent::class.java, InventoryEventHandler) { e ->
        val holder = e.clickedInventory?.holder ?: return@event
        if (e.clickedInventory?.holder != this) return@event
        onInventoryClicked(e)
    }

    val closeInventoryEventDetector = DSLEvent.event(InventoryCloseEvent::class.java, InventoryEventHandler) {
        if (it.inventory != inventory) return@event
        onInventoryClose(it)
        InventoryEventHandler.onDisable()
        inventory?.close()
        lifecycleScope.cancel()
    }

    abstract val playerMenuUtility: IPlayerHolder

    object InventoryNotInitializedException : Exception("Inventory not initialized")

    private var inventory: Inventory? = null
    override fun getInventory(): Inventory = inventory ?: throw InventoryNotInitializedException

    /**
     * Title of this inventory
     */
    abstract var menuTitle: String

    /**
     * Size of inventory
     */
    abstract val menuSize: AstraMenuSize

    /**
     * Menu handler
     */
    abstract fun onInventoryClicked(e: InventoryClickEvent)

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

    /**
     * Called when inventory was closed
     */
    abstract fun onInventoryClose(it: InventoryCloseEvent)
    abstract fun onCreated()
}
