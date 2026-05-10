package ru.astrainteractive.astralibs.menu.paginator.model

import ru.astrainteractive.astralibs.menu.paginator.api.Paginator

/**
 * Snapshot of the current pagination state used by [Paginator].
 *
 * @param page Zero-based index of the current page.
 * @param maxItems Total number of items across all pages.
 * @param maxItemsPerPage Maximum number of items shown per page.
 */
data class PaginatorContext(
    val page: Int,
    val maxItems: Int,
    val maxItemsPerPage: Int,
)

/** Total number of pages (integer division of [PaginatorContext.maxItems] by [PaginatorContext.maxItemsPerPage]). */
val PaginatorContext.maxPages: Int
    get() = maxItems / maxItemsPerPage

val PaginatorContext.isFirstPage: Boolean
    get() = page == 0

val PaginatorContext.isLastPage: Boolean
    get() = page >= maxPages

/**
 * Converts a within-page slot index [i] to the absolute index in the full item list.
 */
fun PaginatorContext.indexOfSlot(i: Int): Int = maxItemsPerPage * page + i
