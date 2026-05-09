package ru.astrainteractive.astralibs.menu.inventory.api

import kotlinx.coroutines.CoroutineScope
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import ru.astrainteractive.astralibs.menu.clicker.ClickListener
import ru.astrainteractive.astralibs.menu.clicker.DefaultClickListener
import ru.astrainteractive.astralibs.menu.core.Menu
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent
import ru.astrainteractive.astralibs.menu.inventory.model.InventorySize
import ru.astrainteractive.klibs.mikro.core.coroutines.CoroutineFeature

/**
 * Default [Menu] implementation backed by a chest-style Bukkit [Inventory] sized via [inventorySize].
 *
 * Provides ready-to-use defaults for the stateful collaborators declared by [Menu]:
 * - [clickListener] — a fresh [DefaultClickListener]
 * - [menuScope] — an unconfined [CoroutineScope] cancelled with the menu
 *
 * Register a [DefaultInventoryClickEvent] listener on plugin startup so click/close events are dispatched here.
 */
abstract class InventoryMenu : Menu {

    /**
     * Size of the inventory created for this menu.
     */
    abstract val inventorySize: InventorySize

    override val clickListener: ClickListener = DefaultClickListener()

    override val menuScope: CoroutineScope = CoroutineFeature.Unconfined

    private val _inventory: Inventory by lazy {
        Bukkit.createInventory(this, inventorySize.size, title)
    }

    override fun getInventory(): Inventory = _inventory
}
