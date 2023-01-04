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
import org.bukkit.event.inventory.InventoryType
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

    val inventoryEventHandler = object : EventManager {
        override val handlers: MutableList<EventListener> = mutableListOf()
    }

    fun IInventoryButton.setInventoryButton() {
        inventory?.setItem(index, item)
    }

    val onClickDetector = DSLEvent.event<InventoryClickEvent>(inventoryEventHandler) {
        onInventoryClicked(it)
    }

    val closeInventoryEventDetector = DSLEvent.event<InventoryCloseEvent>(inventoryEventHandler) {
        onInventoryClose(it)
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
     * Called when inventory was closed
     */
    fun onInventoryClose(it: InventoryCloseEvent) {
        val topInventory = it.view.topInventory
        if (topInventory == inventory || topInventory.holder == this)
            clear()
    }

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

    override fun clear() {
        super.clear()
        // Stop handler
        inventoryEventHandler.onDisable()
        // Stop lifecycle
        lifecycleScope.cancel()
        // Stop click event listener
        onClickDetector.also(EventListener::onDisable)
        onClickDetector.also(HandlerList::unregisterAll)
        onClickDetector.also(InventoryCloseEvent.getHandlerList()::unregister)
        // Stop close inventory listener
        closeInventoryEventDetector.also(EventListener::onDisable)
        closeInventoryEventDetector.also(HandlerList::unregisterAll)
        closeInventoryEventDetector.also(InventoryCloseEvent.getHandlerList()::unregister)
    }

    abstract fun onCreated()
}
