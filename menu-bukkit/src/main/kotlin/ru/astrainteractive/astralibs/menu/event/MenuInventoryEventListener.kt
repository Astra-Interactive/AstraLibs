package ru.astrainteractive.astralibs.menu.event

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import ru.astrainteractive.astralibs.menu.gui.Menu

internal class MenuInventoryEventListener : DefaultInventoryClickEvent {
    @EventHandler
    fun onInventoryClickEvent(e: InventoryClickEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryClickEvent(e)
    }

    @EventHandler
    fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryCloseEvent(e)
    }

    @EventHandler
    fun onInventoryOpenEvent(e: InventoryOpenEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryOpenEvent(e)
    }
}
