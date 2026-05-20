package ru.astrainteractive.astralibs.menu.inventory.api

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import ru.astrainteractive.astralibs.menu.clicker.ClickListener
import ru.astrainteractive.astralibs.menu.clicker.DefaultClickListener
import ru.astrainteractive.astralibs.menu.core.Menu
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent
import ru.astrainteractive.astralibs.menu.inventory.model.InventorySize

/**
 * Default [Menu] backed by a chest-style Bukkit [Inventory].
 *
 * Requires a [DefaultInventoryClickEvent] listener registered once on plugin startup.
 */
abstract class InventoryMenu : Menu() {

    abstract val inventorySize: InventorySize

    override val clickListener: ClickListener = DefaultClickListener()

    private val _inventory: Inventory by lazy {
        Bukkit.createInventory(this, inventorySize.size, title)
    }

    override fun getInventory(): Inventory = _inventory
}
