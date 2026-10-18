package ru.astrainteractive.astralibs.menu.paginator

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.astrainteractive.astralibs.menu.paginator.api.Paginator
import ru.astrainteractive.astralibs.menu.paginator.model.PaginatorContext
import ru.astrainteractive.astralibs.menu.paginator.model.maxPages

/** Default [Paginator] backed by a [MutableStateFlow]. Validates bounds on every [openPage] call. */
public class DefaultPaginator(
    page: Int = 0,
    maxItems: Int = 0,
    maxItemsPerPage: Int = 0
) : Paginator {
    private val _paginatorContextStateFlow = MutableStateFlow(
        PaginatorContext(
            page = page,
            maxItems = maxItems,
            maxItemsPerPage = maxItemsPerPage
        )
    )
    override val paginatorContextStateFlow: StateFlow<PaginatorContext> = _paginatorContextStateFlow.asStateFlow()

    override fun openPage(page: Int) {
        _paginatorContextStateFlow.update { paginatorContext ->
            check(page <= paginatorContext.maxPages) {
                "You are trying to load page $page when only ${paginatorContext.maxPages} available"
            }
            check(page >= 0) {
                "You are trying to load page $page which is negative"
            }
            paginatorContext.copy(page = page)
        }
    }

    override fun update(block: (PaginatorContext) -> PaginatorContext) {
        _paginatorContextStateFlow.update(block)
    }
}
