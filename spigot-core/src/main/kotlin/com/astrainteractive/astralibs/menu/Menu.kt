package com.astrainteractive.astralibs.menu

import com.astrainteractive.astralibs.async.PluginScope
import com.astrainteractive.astralibs.async.BukkitMain
import com.astrainteractive.astralibs.events.DSLEvent
import com.astrainteractive.astralibs.events.EventListener
import com.astrainteractive.astralibs.events.EventManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder


/**
 * Default menu abstract class
 * Don't forget to add [MenuListener]
 */
@SuppressWarnings("Don't forget to add MenuListener")
abstract class Menu : InventoryHolder {


    abstract val playerMenuUtility: AstraPlayerMenuUtility

    private lateinit var inventory: Inventory
    fun isInventoryInitialized() = this::inventory.isInitialized

    /**
     * Title of this inventory
     */
    abstract var menuName: String

    /**
     * Size of inventory
     */
    abstract val menuSize: AstraMenuSize

    /**
     * Menu handler
     */
    abstract fun handleMenu(e: InventoryClickEvent)

    /**
     * Function for setting items in menu
     */
    abstract fun setMenuItems()

    /**
     * Open inventory method for Menu class
     */
    fun open() {
        inventory = Bukkit.createInventory(this, menuSize.size, menuName)
        PluginScope.launch(Dispatchers.BukkitMain) {
            playerMenuUtility.player.openInventory(inventory)
            onCreated()
        }
    }

    override fun getInventory() = inventory

    /**
     * Inventory close manager
     */
    private inner class CloseInventoryEventManager : EventManager {
        override val handlers: MutableList<EventListener> = mutableListOf()
        private val menuCloseHandler = DSLEvent.event(InventoryCloseEvent::class.java, this) {
            onDestroy(it,this)
            this.onDisable()
        }
    }
    private val inventoryCloseManager = CloseInventoryEventManager()

    /**
     * Called when inventory was closed
     */
    abstract fun onDestroy(it: InventoryCloseEvent, manager: EventManager)
    fun onCreated(){
        setMenuItems()
    }

}