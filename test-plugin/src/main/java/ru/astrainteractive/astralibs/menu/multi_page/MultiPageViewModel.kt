package ru.astrainteractive.astralibs.menu.multi_page

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import ru.astrainteractive.astralibs.architecture.ViewModel

class MultiPageViewModel : ViewModel() {
    private val generateList: List<ItemStack>
        get() = Material.values().toList().shuffled().subList(0, 120).map {
            ItemStack(it)
        }
    val items = MutableStateFlow<List<ItemStack>>(generateList)
    fun onClicked() {
        items.update { generateList }
    }
}