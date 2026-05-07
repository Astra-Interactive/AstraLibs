package ru.astrainteractive.astralibs.menu.paginator.api

import kotlinx.coroutines.flow.StateFlow
import ru.astrainteractive.astralibs.menu.paginator.model.PaginatorContext

interface Paginator {
    val paginatorContextStateFlow: StateFlow<PaginatorContext>

    fun openPage(page: Int)

    fun openNextPage()

    fun openPrevPage()
}
