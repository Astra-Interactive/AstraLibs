package ru.astrainteractive.astralibs.menu

import org.bukkit.event.inventory.InventoryClickEvent


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
    fun handleChangePageClick(slot: Int) {
        if (slot == prevPageButton.index)
            if (isFirstPage) return
            else loadPage(-1)
        else if (slot == nextPageButton.index)
            if (isLastPage) return
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
    val isFirstPage: Boolean
        get() = page == 0

    /**
     * Check for last page
     */
    val isLastPage: Boolean
        get() = page >= maxPages

    /**
     * Function for handling pages
     */
    open fun loadPage(next: Int) {
        page += next
        onPageChanged()
    }

    abstract fun onPageChanged()

    abstract val prevPageButton: IInventoryButton
    abstract val backPageButton: IInventoryButton
    abstract val nextPageButton: IInventoryButton


    /**
     * Managing buttons for pages
     *
     * next,prev,back
     */
    fun setManageButtons() {
        if (page >= 1)
            prevPageButton.setInventoryButton()

        backPageButton.setInventoryButton()

        if (page < maxPages)
            nextPageButton.setInventoryButton()

    }
}
