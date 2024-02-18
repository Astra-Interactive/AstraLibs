package ru.astrainteractive.astralibs.menu.inventory

import ru.astrainteractive.astralibs.menu.inventory.model.PageContext
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

abstract class PaginatedInventoryMenu : InventoryMenu() {
    abstract var pageContext: PageContext
    abstract val prevPageButton: InventorySlot
    abstract val nextPageButton: InventorySlot
}
