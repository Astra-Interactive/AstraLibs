package ru.astrainteractive.astralibs.menu.paginator.api

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.astralibs.menu.paginator.model.PaginatorContext

/** Controls page navigation for a paged inventory menu. Collect [paginatorContextStateFlow] to re-render on changes. */
public interface Paginator {
    public val paginatorContextStateFlow: StateFlow<PaginatorContext>

    /**
     * Navigates to [page].
     *
     * @throws IllegalStateException if [page] is negative or exceeds [PaginatorContext.maxPages].
     */
    public fun openPage(page: Int)

    public fun update(block: (PaginatorContext) -> PaginatorContext)
}

public val Paginator.context: PaginatorContext
    get() = paginatorContextStateFlow.value

/** @throws IllegalStateException if already on the last page. */
public fun Paginator.openNextPage() {
    openPage(paginatorContextStateFlow.value.page + 1)
}

/** @throws IllegalStateException if already on page 0. */
public fun Paginator.openPrevPage() {
    openPage(paginatorContextStateFlow.value.page - 1)
}

public fun Paginator.setMaxItems(maxItems: Int) {
    update { context -> context.copy(maxItems = maxItems) }
}

public fun Paginator.setMaxItemsPerPage(maxItemsPerPage: Int) {
    update { context -> context.copy(maxItemsPerPage = maxItemsPerPage) }
}
