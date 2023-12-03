package ru.astrainteractive.astralibs.menu.menu

abstract class PaginatedMenu : Menu() {
    /**
     * Page of current menu. Must be 0 by default
     */
    abstract var page: Int

    /**
     * Max items in this menu
     */
    abstract val maxItemsAmount: Int

    abstract fun onPageChanged()

    abstract val prevPageButton: InventorySlot

    abstract val backPageButton: InventorySlot

    abstract val nextPageButton: InventorySlot

    /**
     * Max items allowed in current page. No more than 45 for paginated. Final row is for pagination
     */
    open val maxItemsPerPage
        get() = menuSize.size - 9

    /**
     * Max pages in this menu
     */
    open val maxPages
        get() = maxItemsAmount / maxItemsPerPage

    override fun render() {
        super.render()
        setManageButtons()
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
     * Index of current item
     */
    fun getIndex(i: Int): Int {
        return maxItemsPerPage * page + i
    }

    /**
     * Function for handling pages
     */
    fun showPage(page: Int) {
        check(page <= maxPages) {
            "You are trying to load page $page when only $maxPages avaliable"
        }
        check(page >= 0) {
            "You are trying to load page $page which is negative"
        }
        this.page = page
        onPageChanged()
    }

    /**
     * Shows next page
     */
    fun showNextPage() {
        showPage(page + 1)
    }

    /**
     * Shows previous page
     */
    fun showPrevPage() {
        showPage(page - 1)
    }

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
    fun setManageButtons() {
        if (page >= 1) prevPageButton.setInventorySlot()
        backPageButton.setInventorySlot()
        if (page < maxPages) nextPageButton.setInventorySlot()
    }
}
