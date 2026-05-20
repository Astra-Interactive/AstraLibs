package ru.astrainteractive.astralibs.menu.paginator.api

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.astralibs.menu.paginator.model.PaginatorContext

/** Controls page navigation for a paged inventory menu. Collect [paginatorContextStateFlow] to re-render on changes. */
interface Paginator {
    val paginatorContextStateFlow: StateFlow<PaginatorContext>

    /**
     * Navigates to [page].
     *
     * @throws IllegalStateException if [page] is negative or exceeds [PaginatorContext.maxPages].
     */
    fun openPage(page: Int)

    fun update(block: (PaginatorContext) -> PaginatorContext)
}

val Paginator.context: PaginatorContext
    get() = paginatorContextStateFlow.value

/** @throws IllegalStateException if already on the last page. */
fun Paginator.openNextPage() {
    openPage(paginatorContextStateFlow.value.page + 1)
}

/** @throws IllegalStateException if already on page 0. */
fun Paginator.openPrevPage() {
    openPage(paginatorContextStateFlow.value.page - 1)
}

fun Paginator.setMaxItems(maxItems: Int) {
    update { context -> context.copy(maxItems = maxItems) }
}

fun Paginator.setMaxItemsPerPage(maxItemsPerPage: Int) {
    update { context -> context.copy(maxItemsPerPage = maxItemsPerPage) }
}
