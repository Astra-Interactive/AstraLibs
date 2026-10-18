package ru.astrainteractive.astralibs.menu.inventory.gui

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import ru.astrainteractive.astralibs.menu.clicker.api.ClickListener
import ru.astrainteractive.astralibs.menu.clicker.component.DefaultClickListener
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent
import ru.astrainteractive.astralibs.menu.gui.Menu
import ru.astrainteractive.astralibs.menu.inventory.model.InventorySize

/**
 * Default [Menu] backed by a chest-style Bukkit [Inventory].
 *
 * Requires a [DefaultInventoryClickEvent] listener registered once on plugin startup.
 */
public abstract class InventoryMenu : Menu() {

    public abstract val inventorySize: InventorySize

    override val clickListener: ClickListener = DefaultClickListener()

    private val _inventory: Inventory by lazy {
        Bukkit.createInventory(this, inventorySize.size, title)
    }

    override fun getInventory(): Inventory = _inventory
}
