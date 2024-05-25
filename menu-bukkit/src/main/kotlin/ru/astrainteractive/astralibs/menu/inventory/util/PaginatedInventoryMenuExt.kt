package ru.astrainteractive.astralibs.menu.inventory.util

import ru.astrainteractive.astralibs.menu.inventory.PaginatedInventoryMenu
import ru.astrainteractive.astralibs.menu.inventory.util.PageContextExt.maxPages

object PaginatedInventoryMenuExt {
    /**
     * Show [page] in current [PaginatedInventoryMenu]
     *
     */
    fun PaginatedInventoryMenu.showPage(page: Int) {
        check(page <= pageContext.maxPages) {
            "You are trying to load page $page when only ${pageContext.maxPages} available"
        }
        check(page >= 0) {
            "You are trying to load page $page which is negative"
        }
        pageContext = pageContext.copy(page = page)
        render()
    }

    fun PaginatedInventoryMenu.showNextPage() {
        showPage(pageContext.page + 1)
    }

    fun PaginatedInventoryMenu.showPrevPage() {
        showPage(pageContext.page - 1)
    }
}
