package com.astrainteractive.astralibs.menu

import com.astrainteractive.astralibs.async.AsyncHelper
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder


/**
 * Default menu abstract class
 */
abstract class Menu() : InventoryHolder {
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
        setMenuItems()
        AsyncHelper.callSyncMethod {
            playerMenuUtility.player.openInventory(inventory)
        }
    }
    override fun getInventory() = inventory


}