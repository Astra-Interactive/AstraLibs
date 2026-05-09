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

    fun update(block: (PaginatorContext) -> PaginatorContext)
}

val Paginator.context: PaginatorContext
    get() = paginatorContextStateFlow.value

fun Paginator.openNextPage() {
    openPage(paginatorContextStateFlow.value.page + 1)
}

fun Paginator.openPrevPage() {
    openPage(paginatorContextStateFlow.value.page - 1)
}

fun Paginator.setMaxItems(maxItems: Int) {
    update { context -> context.copy(maxItems = maxItems) }
}

fun Paginator.setMaxItemsPerPage(maxItemsPerPage: Int) {
    update { context -> context.copy(maxItemsPerPage = maxItemsPerPage) }
}
