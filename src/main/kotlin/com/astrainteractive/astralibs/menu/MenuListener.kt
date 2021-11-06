package com.astrainteractive.astralibs.menu

import com.astrainteractive.astralibs.IAstraListener
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent

/**
 * You probably won't ever edit this file
 */
class MenuListener : IAstraListener {

    /**
     * Cancelling inventory event if player clicked
     */
    @EventHandler
    fun onMenuClick(e: InventoryClickEvent) {
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