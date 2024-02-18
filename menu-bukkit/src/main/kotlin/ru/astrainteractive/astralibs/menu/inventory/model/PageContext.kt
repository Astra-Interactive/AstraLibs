package ru.astrainteractive.astralibs.menu.inventory.model

import ru.astrainteractive.astralibs.menu.inventory.PaginatedInventoryMenu

/**
 * Context of current page in [PaginatedInventoryMenu]
 *
 * @param page Page of current menu. Must be 0 by default
 * @param maxItems items in this menu
 * @param maxItemsPerPage items allowed in current page
 */
data class PageContext(
    val page: Int,
    val maxItems: Int,
    val maxItemsPerPage: Int,
)
