package ru.astrainteractive.astralibs.menu.inventory

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import ru.astrainteractive.astralibs.menu.core.Menu
import ru.astrainteractive.astralibs.menu.core.MenuSize
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent

/**
 * Default menu abstract class
 * Don't forget to add [DefaultInventoryClickEvent]
 */
abstract class InventoryMenu : Menu() {
    /**
     * Size of inventory
     */
    abstract val inventorySize: MenuSize

    private val _inventory: Inventory by lazy {
        Bukkit.createInventory(this, inventorySize.size, title)
    }

    override fun getInventory(): Inventory = _inventory
}
