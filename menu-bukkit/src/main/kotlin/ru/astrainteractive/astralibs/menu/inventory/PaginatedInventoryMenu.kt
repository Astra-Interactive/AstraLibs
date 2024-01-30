package ru.astrainteractive.astralibs.menu.inventory

import ru.astrainteractive.astralibs.menu.inventory.model.PageContext
import ru.astrainteractive.astralibs.menu.inventory.util.PageContextExt.maxPages
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

abstract class PaginatedInventoryMenu : InventoryMenu() {
    abstract var pageContext: PageContext
    abstract val prevPageButton: InventorySlot
    abstract val nextPageButton: InventorySlot

    abstract fun onPageChanged()

    fun showPage(page: Int) {
        check(page <= pageContext.maxPages) {
            "You are trying to load page $page when only ${pageContext.maxPages} available"
        }
        check(page >= 0) {
            "You are trying to load page $page which is negative"
        }
        pageContext = pageContext.copy(page = page)
        onPageChanged()
    }

    fun showNextPage() {
        showPage(pageContext.page + 1)
    }

    fun showPrevPage() {
        showPage(pageContext.page - 1)
    }
}
