package ru.astrainteractive.astralibs.menu.paginator.model

/** Snapshot of the current pagination state used by [ru.astrainteractive.astralibs.menu.paginator.api.Paginator]. */
public data class PaginatorContext(
    val page: Int,
    val maxItems: Int,
    val maxItemsPerPage: Int,
)

public val PaginatorContext.maxPages: Int
    get() = maxItems / maxItemsPerPage

public val PaginatorContext.isFirstPage: Boolean
    get() = page == 0

public val PaginatorContext.isLastPage: Boolean
    get() = page >= maxPages

/** Converts a within-page slot index to the absolute index in the full item list. */
public fun PaginatorContext.indexOfSlot(i: Int): Int = maxItemsPerPage * page + i
