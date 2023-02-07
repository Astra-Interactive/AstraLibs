package ru.astrainteractive.astralibs.menu.menu

import ru.astrainteractive.astralibs.menu.utils.InventoryButton
import ru.astrainteractive.astralibs.menu.utils.click.ClickListener


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
    fun showPage(page: Int) {
        if (page > maxPages)
            throw IllegalStateException("You are trying to load page $page when only $maxPages avaliable")
        if (page < 0)
            throw IllegalStateException("You are trying to load page $page which is negative")
        this.page = page
        onPageChanged()
    }

    abstract fun onPageChanged()

    abstract val prevPageButton: InventoryButton
    abstract val backPageButton: InventoryButton
    abstract val nextPageButton: InventoryButton


    /**
     * This function will set:
     *
     * [backPageButton];
     *
     * [prevPageButton] if there is previous pages;
     *
     * [nextPageButton] if there is next pages
     *
     * Also it will remember clicks
     */
    fun setManageButtons(clickListener: ClickListener) {
        if (page >= 1)
            prevPageButton.also {
                it.setInventoryButton()
                clickListener.remember(it)
            }

        backPageButton.also {
            it.setInventoryButton()
            clickListener.remember(it)
        }

        if (page < maxPages)
            nextPageButton.also {
                it.setInventoryButton()
                clickListener.remember(it)
            }

    }
}
