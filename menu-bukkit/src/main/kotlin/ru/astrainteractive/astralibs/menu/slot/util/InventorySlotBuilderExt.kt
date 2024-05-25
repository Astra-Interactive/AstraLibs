@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.menu.slot.util

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import ru.astrainteractive.astralibs.menu.clicker.Click
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

object InventorySlotBuilderExt {
    /**
     * Set new material for current [InventorySlot.Builder.itemStack]
     */
    fun InventorySlot.Builder.setMaterial(material: Material): InventorySlot.Builder {
        itemStack.type = material
        return this
    }

    /**
     * Replace current ItemStack with new
     */
    fun InventorySlot.Builder.setItemStack(itemStack: ItemStack): InventorySlot.Builder {
        this.itemStack = itemStack
        return this
    }

    /**
     * Set inventory index of an item
     */
    fun InventorySlot.Builder.setIndex(index: Int): InventorySlot.Builder {
        this.index = index
        return this
    }

    /**
     * Set click listener for an item
     */
    fun InventorySlot.Builder.setOnClickListener(click: Click): InventorySlot.Builder {
        this.click = click
        return this
    }

    /**
     * Edit ItemStack
     */
    fun InventorySlot.Builder.editItemStack(block: ItemStack.() -> Unit): InventorySlot.Builder {
        itemStack = itemStack.apply(block)
        return this
    }

    /**
     * Edit ItemMeta
     */
    fun InventorySlot.Builder.editMeta(block: ItemMeta.() -> Unit): InventorySlot.Builder {
        itemStack.itemMeta = itemStack.itemMeta.apply(block)
        return this
    }

    /**
     * Set amount of ItemStack
     */
    fun InventorySlot.Builder.setAmount(amount: Int): InventorySlot.Builder {
        itemStack.amount = amount
        return this
    }

    /**
     * Set displayName of ItemStack
     */
    fun InventorySlot.Builder.setDisplayName(string: String): InventorySlot.Builder {
        editMeta {
            this.setDisplayName(string)
        }
        return this
    }

    /**
     * Set displayName of ItemStack
     */
    fun InventorySlot.Builder.setDisplayName(component: Component): InventorySlot.Builder {
        editMeta {
            displayName(component)
        }
        return this
    }

    /**
     * Set lore of ItemStack
     */
    fun InventorySlot.Builder.setLore(loreComponents: List<Component>): InventorySlot.Builder {
        editMeta {
            lore(loreComponents)
        }
        return this
    }

    /**
     * Add lore line into ItemStack
     */
    fun InventorySlot.Builder.addLore(loreComponent: Component): InventorySlot.Builder {
        editMeta {
            val currentLore = lore().orEmpty().toMutableList()
            currentLore.add(loreComponent)
            lore(currentLore)
        }
        return this
    }

    /**
     * Add a predicate for computed builder action
     */
    fun InventorySlot.Builder.predicate(
        condition: Boolean,
        block: InventorySlot.Builder.() -> Unit
    ): InventorySlot.Builder {
        if (condition) block.invoke(this)
        return this
    }
}
