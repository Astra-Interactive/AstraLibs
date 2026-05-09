@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.menu.slot

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import ru.astrainteractive.astralibs.menu.clicker.Click
import kotlin.collections.orEmpty
import kotlin.collections.toMutableList

/**
 * An inventory slot: a visual [item] placed at [index] that fires [click] when clicked.
 *
 * Build instances with [Builder].
 */
interface InventorySlot {
    val item: ItemStack
    val index: Int
    val click: Click

    /** Mutable builder for [InventorySlot]. */
    class Builder {
        var itemStack = ItemStack(Material.AIR)
        var index = 0
        var click: Click = Click.Empty

        fun build(): InventorySlot = object : InventorySlot {
            override val item: ItemStack = this@Builder.itemStack
            override val index: Int = this@Builder.index
            override val click: Click = this@Builder.click
        }
    }
}

fun InventorySlot.Builder.setMaterial(material: Material): InventorySlot.Builder {
    itemStack.type = material
    return this
}

fun InventorySlot.Builder.setItemStack(itemStack: ItemStack): InventorySlot.Builder {
    this.itemStack = itemStack
    return this
}

fun InventorySlot.Builder.setIndex(index: Int): InventorySlot.Builder {
    this.index = index
    return this
}

fun InventorySlot.Builder.setOnClickListener(click: Click): InventorySlot.Builder {
    this.click = click
    return this
}

fun InventorySlot.Builder.editItemStack(block: ItemStack.() -> Unit): InventorySlot.Builder {
    itemStack = itemStack.apply(block)
    return this
}

fun InventorySlot.Builder.editMeta(block: ItemMeta.() -> Unit): InventorySlot.Builder {
    itemStack.itemMeta = itemStack.itemMeta.apply(block)
    return this
}

fun InventorySlot.Builder.setAmount(amount: Int): InventorySlot.Builder {
    itemStack.amount = amount
    return this
}

fun InventorySlot.Builder.setDisplayName(string: String): InventorySlot.Builder {
    editMeta {
        displayName(Component.text(string))
    }
    return this
}

fun InventorySlot.Builder.setDisplayName(component: Component): InventorySlot.Builder {
    editMeta {
        displayName(component)
    }
    return this
}

fun InventorySlot.Builder.setLore(loreComponents: List<Component>): InventorySlot.Builder {
    editMeta {
        lore(loreComponents)
    }
    return this
}

fun InventorySlot.Builder.addLore(loreComponent: Component): InventorySlot.Builder {
    editMeta {
        val currentLore = lore().orEmpty().toMutableList()
        currentLore.add(loreComponent)
        lore(currentLore)
    }
    return this
}

/** Applies [block] to the builder only when [condition] is true. */
fun InventorySlot.Builder.predicate(
    condition: Boolean,
    block: InventorySlot.Builder.() -> Unit
): InventorySlot.Builder {
    if (condition) block.invoke(this)
    return this
}
