package ru.astrainteractive.astralibs.menu.event

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.events.EventListener
import ru.astrainteractive.astralibs.menu.menu.Menu

/**
 * To be able to use inventory events - you need to register this class somewhere. For example in [JavaPlugin.onEnable]
 */
object SharedInventoryClickEvent : EventListener {
    @EventHandler
    fun onInventoryClicked(e: InventoryClickEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return

        holder.onInventoryClicked(e)
    }

    @EventHandler
    fun onInventoryClosed(e: InventoryCloseEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryClose(e)
        holder.close()
    }
}