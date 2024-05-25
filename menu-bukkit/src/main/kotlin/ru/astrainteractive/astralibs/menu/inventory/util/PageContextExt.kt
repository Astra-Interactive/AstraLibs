package ru.astrainteractive.astralibs.menu.inventory.util

import ru.astrainteractive.astralibs.menu.inventory.model.PageContext

object PageContextExt {

    /**
     * Max pages in this menu
     */
    val PageContext.maxPages: Int
        get() = maxItems / maxItemsPerPage

    /**
     * Check for first page
     */
    val PageContext.isFirstPage: Boolean
        get() = page == 0

    /**
     * Check for last page
     */
    val PageContext.isLastPage: Boolean
        get() = page >= maxPages

    /**
     * Index of current item
     *
     * @param i is the slot index of item inside inventory
     * @return the index of item inside your items list considering [PageContext.maxItemsPerPage]
     */
    fun PageContext.getIndex(i: Int): Int = maxItemsPerPage * page + i
}
