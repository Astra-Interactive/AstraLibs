package ru.astrainteractive.astralibs.menu.menu

import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.async.AsyncComponent
import ru.astrainteractive.astralibs.menu.clicker.ClickListener
import ru.astrainteractive.astralibs.menu.clicker.MenuClickListener
import ru.astrainteractive.astralibs.menu.holder.PlayerHolder

/**
 * Default menu abstract class
 * Don't forget to add [MenuListener]
 */
@SuppressWarnings("Don't forget to add MenuListener")
abstract class Menu : InventoryHolder, AsyncComponent() {

    private val clickListener: ClickListener = MenuClickListener()

    private var inventory: Inventory? = null

    override fun getInventory(): Inventory = checkNotNull(inventory) { "Inventory not initialized" }

    abstract val playerHolder: PlayerHolder

    /**
     * Title of this inventory
     */
    abstract var menuTitle: Component

    /**
     * Size of inventory
     */
    abstract val menuSize: MenuSize

    /**
     * Menu handler
     */
    open fun onInventoryClicked(e: InventoryClickEvent) {
        clickListener.onClick(e)
    }

    /**
     * Called when inventory was closed
     *
     * After [onInventoryClose] executed - [componentScope] will be closed
     */
    abstract fun onInventoryClose(it: InventoryCloseEvent)

    /**
     * This method called after inventory created and opened
     */
    abstract fun onCreated()

    /**
     * Render and reset the content of GUI
     */
    open fun render() {
        inventory?.clear()
        clickListener.clearClickListener()
    }

    /**
     * This function will add [InventorySlot] into GUI and remember clickEvent
     */
    fun InventorySlot.setInventorySlot() {
        clickListener.remember(this)
        inventory?.setItem(index, item)
    }

    /**
     * Open inventory method for Menu class
     * Should be executed on main thread
     */
    fun open() {
        inventory = Bukkit.createInventory(this, menuSize.size, menuTitle)
        inventory?.let(playerHolder.player::openInventory)
        onCreated()
    }
}
