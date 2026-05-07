package ru.astrainteractive.astralibs.menu.paginator.model

/**
 * Context of current page in [ru.astrainteractive.astralibs.menu.paginator.api.Paginator]
 *
 * @param page Page of current menu. Must be 0 by default
 * @param maxItems items in this menu
 * @param maxItemsPerPage items allowed in current page
 */
data class PaginatorContext(
    val page: Int,
    val maxItems: Int,
    val maxItemsPerPage: Int,
)

/**
 * Max pages in this menu
 */
val PaginatorContext.maxPages: Int
    get() = maxItems / maxItemsPerPage

/**
 * Check for first page
 */
val PaginatorContext.isFirstPage: Boolean
    get() = page == 0

/**
 * Check for last page
 */
val PaginatorContext.isLastPage: Boolean
    get() = page >= maxPages

/**
 * Index of current item
 *
 * @param i is the slot index of item inside inventory
 * @return the index of item inside your items list considering [PaginatorContext.maxItemsPerPage]
 */
fun PaginatorContext.indexOfSlot(i: Int): Int = maxItemsPerPage * page + i
