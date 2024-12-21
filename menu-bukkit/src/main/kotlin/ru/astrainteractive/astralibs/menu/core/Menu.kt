package ru.astrainteractive.astralibs.menu.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.async.CoroutineFeature
import ru.astrainteractive.astralibs.menu.clicker.ClickListener
import ru.astrainteractive.astralibs.menu.clicker.MenuClickListener
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent
import ru.astrainteractive.astralibs.menu.holder.PlayerHolder
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/**
 * Default menu abstract class
 *
 * Don't forget to add [DefaultInventoryClickEvent]
 */
abstract class Menu : InventoryHolder {

    private val clickListener: ClickListener = MenuClickListener()

    protected val menuScope: CoroutineScope = CoroutineFeature.Unconfined()

    abstract val playerHolder: PlayerHolder

    abstract val title: Component

    open val childComponents: List<CoroutineScope> = emptyList()

    /**
     * This method called after inventory created and opened
     */
    abstract fun onInventoryCreated()

    /**
     * Menu handler
     */
    open fun onInventoryClicked(e: InventoryClickEvent) {
        clickListener.onClick(e)
    }

    /**
     * Called when inventory was closed
     *
     * After [onInventoryClosed] executed - [menuScope] will be closed
     */
    open fun onInventoryClosed(it: InventoryCloseEvent) {
        clickListener.clear()
        childComponents.forEach(CoroutineScope::cancel)
        menuScope.cancel()
    }

    /**
     * Render and reset the content of GUI
     */
    open fun render() {
        clickListener.clear()
        inventory.clear()
    }

    /**
     * Open inventory method for Menu class
     * Should be executed on main thread
     */
    fun open() {
        playerHolder.player.openInventory(inventory)
        onInventoryCreated()
    }

    /**
     * This function will add [InventorySlot] into GUI and remember clickEvent
     */
    fun InventorySlot.setInventorySlot() {
        clickListener.remember(this)
        inventory.setItem(index, item)
    }

    fun InventorySlot.setInventorySlotIf(predicate: () -> Boolean = { true }) {
        if (!predicate.invoke()) return
        setInventorySlot()
    }
}
