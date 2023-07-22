package ru.astrainteractive.astralibs.menu.event

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.async.AsyncComponent
import ru.astrainteractive.astralibs.event.EventListener
import ru.astrainteractive.astralibs.menu.menu.Menu

/**
 * To be able to use inventory events - you need to register this class somewhere. For example in [JavaPlugin.onEnable]
 */
class DefaultInventoryClickEvent : EventListener {
    @EventHandler
    fun onInventoryClicked(e: InventoryClickEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryClicked(e)
    }

    @EventHandler
    fun onInventoryClosed(e: InventoryCloseEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryClose(e)
        (holder as AsyncComponent).close()
    }
}
