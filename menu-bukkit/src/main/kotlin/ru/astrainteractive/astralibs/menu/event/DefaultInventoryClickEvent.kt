package ru.astrainteractive.astralibs.menu.event

import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.event.EventListener
import ru.astrainteractive.astralibs.menu.core.Menu

/**
 * Bukkit event listener that routes inventory events to the active [Menu]. Register once in [JavaPlugin.onEnable].
 */
public class DefaultInventoryClickEvent : EventListener {
    @EventHandler
    public fun onInventoryClickEvent(e: InventoryClickEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryClickEvent(e)
    }

    @EventHandler
    public fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryCloseEvent(e)
    }

    @EventHandler
    public fun onInventoryOpenEvent(e: InventoryOpenEvent) {
        val holder = e.view.topInventory.holder as? Menu ?: return
        holder.onInventoryOpenEvent(e)
    }
}
