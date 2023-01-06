package ru.astrainteractive.astralibs.menu

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import ru.astrainteractive.astralibs.events.EventListener

/**
 * To be able to use inventory events - you need to register this class
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