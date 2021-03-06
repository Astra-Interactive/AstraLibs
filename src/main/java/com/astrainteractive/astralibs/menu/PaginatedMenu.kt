package com.astrainteractive.astralibs.menu

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack


abstract class PaginatedMenu : Menu() {

    /**
     * Page of current menu. Must be 0 by default
     */
    abstract var page: Int

    /**
     * Max items allowed in current page. No more than 45 for paginated. Final row is for pagination
     */
    open val maxItemsPerPage
        get() = menuSize.size - 9

    /**
     * Max items in this menu
     */
    abstract val maxItemsAmount: Int

    /**
     * Max pages in this menu
     */
    val maxPages
        get() = maxItemsAmount / maxItemsPerPage

    /**
     * Standart menu handler for close/back/next
     */
    override fun handleMenu(e: InventoryClickEvent) {
        if (e.slot == prevButtonIndex)
            if (isFirstPage()) return
            else loadPage(-1)
        else if (e.slot == nextButtonIndex)
            if (isLastPage()) return
            else loadPage(1)
    }

    /**
     * Index of current item
     */
    fun getIndex(i: Int): Int {
        return maxItemsPerPage * page + i
    }

    /**
     * Check for first page
     */
    fun isFirstPage(): Boolean {
        if (page == 0)
            return true
        return false
    }

    /**
     * Check for last page
     */
    fun isLastPage(): Boolean {
        if (page >= maxPages)
            return true
        return false
    }

    /**
     * Function for handling pages
     */
    open fun loadPage(next: Int) {
        page += next
        inventory.clear()
        setMenuItems()
    }

    abstract val prevPageButton: ItemStack
    abstract val backPageButton: ItemStack
    abstract val nextPageButton: ItemStack


    abstract val prevButtonIndex: Int
    abstract val backButtonIndex: Int
    abstract val nextButtonIndex: Int
    

    /**
     * Managing buttons for pages
     *
     * next,prev,back
     */
    fun addManageButtons() {
        if (page >= 1)
            inventory.setItem(
                prevButtonIndex,
                prevPageButton
            )

        inventory.setItem(
            backButtonIndex,
            backPageButton
        )

        if (page < maxPages)
            inventory.setItem(
                nextButtonIndex,
                nextPageButton
            )

    }
}
