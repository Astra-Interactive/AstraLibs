package com.astrainteractive.astralibs.menu

import com.astrainteractive.astralibs.EventListener
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * You probably won't ever edit this file
 */
open class MenuListener : EventListener {

    /**
     * Cancelling inventory event if player clicked
     */
    @EventHandler
    open fun onMenuClick(e: InventoryClickEvent) {
        val holder = e.clickedInventory?.holder?:return
        if (e.view.topInventory.holder is Menu)
            e.isCancelled = true
        if (holder is Menu || holder is PaginatedMenu)
            e.isCancelled = true
        if (holder is Menu) {
            e.currentItem?:return
            holder.handleMenu(e)
        }
    }
    public override fun onDisable(){
        InventoryClickEvent.getHandlerList().unregister(this)
    }
}

