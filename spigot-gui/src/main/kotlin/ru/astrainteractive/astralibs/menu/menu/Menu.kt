package ru.astrainteractive.astralibs.menu.menu

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
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
abstract class Menu : InventoryHolder {
    val menuScope: CoroutineScope = AsyncComponent.Default()

    open val childComponents: List<CoroutineScope> = emptyList()

    abstract val playerHolder: PlayerHolder

    /**
     * Title of this inventory
     */
    abstract var menuTitle: Component

    /**
     * Size of inventory
     */
    abstract val menuSize: MenuSize

    private val clickListener: ClickListener = MenuClickListener()

    private val menuInventory: Inventory by lazy {
        Bukkit.createInventory(this, menuSize.size, menuTitle)
    }

    override fun getInventory(): Inventory = menuInventory

    /**
     * This method called after inventory created and opened
     */
    abstract fun onCreated()

    /**
     * Menu handler
     */
    open fun onInventoryClicked(e: InventoryClickEvent) {
        clickListener.onClick(e)
    }

    /**
     * Called when inventory was closed
     *
     * After [onInventoryClose] executed - [menuScope] will be closed
     */
    open fun onInventoryClose(it: InventoryCloseEvent) {
        menuScope.cancel()
        childComponents.forEach(CoroutineScope::cancel)
        clickListener.clear()
    }

    /**
     * Render and reset the content of GUI
     */
    open fun render() {
        inventory.clear()
        clickListener.clear()
    }

    /**
     * This function will add [InventorySlot] into GUI and remember clickEvent
     */
    fun InventorySlot.setInventorySlot() {
        clickListener.remember(this)
        inventory.setItem(index, item)
    }

    /**
     * Open inventory method for Menu class
     * Should be executed on main thread
     */
    fun open() {
        playerHolder.player.openInventory(inventory)
        onCreated()
    }
}
