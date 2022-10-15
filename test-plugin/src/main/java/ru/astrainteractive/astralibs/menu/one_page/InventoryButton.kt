package ru.astrainteractive.astralibs.menu.one_page

import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.menu.IInventoryButton

data class InventoryButton(override val item: ItemStack, override val index: Int = 4) : IInventoryButton