package ru.astrainteractive.astralibs.menu.one_page

import ru.astrainteractive.astralibs.architecture.IBaseView

interface IInventoryView : IBaseView {
    fun showInventoryButton(inventoryButton: InventoryButton)
}