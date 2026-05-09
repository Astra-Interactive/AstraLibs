package ru.astrainteractive.astralibs.menu.paginator.api

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.astralibs.menu.paginator.model.PaginatorContext

/**
 * Controls page navigation for a paged inventory menu.
 *
 * Collect [paginatorContextStateFlow] in the menu to re-render whenever the page changes.
 */
interface Paginator {
    val paginatorContextStateFlow: StateFlow<PaginatorContext>

    fun openPage(page: Int)

    fun openNextPage()

    fun openPrevPage()

    fun update(block: (PaginatorContext) -> PaginatorContext)
}
