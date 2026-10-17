package ru.astrainteractive.astralibs.menu.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.menu.clicker.ClickListener
import ru.astrainteractive.astralibs.menu.clicker.DefaultClickListener
import ru.astrainteractive.astralibs.menu.clicker.remember
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent
import ru.astrainteractive.astralibs.menu.slot.InventorySlot

/**
 * Contract of an interactive Bukkit menu.
 *
 * Requires a [DefaultInventoryClickEvent] listener registered once on plugin startup.
 */
public abstract class Menu : InventoryHolder {
    public abstract val title: Component

    /** Coroutine scope cancelled automatically when the inventory closes. */
    public abstract val menuScope: CoroutineScope

    /** Additional scopes cancelled together with [menuScope] on close. */
    public abstract val childComponents: List<CoroutineScope>

    public open val clickListener: ClickListener = DefaultClickListener()

    public open fun onInventoryClickEvent(e: InventoryClickEvent) {
        clickListener.onClick(e)
    }

    /** Clears click handlers and cancels all coroutine scopes. Always call `super` when overriding. */
    public open fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        clickListener.clear()
        childComponents
            .plus(menuScope)
            .forEach(CoroutineScope::cancel)
    }

    public open fun render() {
        clear()
    }

    public abstract fun onInventoryOpenEvent(e: InventoryOpenEvent)
}

/** Registers [slot]'s click handler and writes its item into the inventory. */
public fun Menu.setInventorySlot(slot: InventorySlot) {
    clickListener.remember(slot)
    inventory.setItem(slot.index, slot.item)
}

/** Registers click handlers and writes items for all [slots] into the inventory. */
public fun Menu.setInventorySlot(slots: Iterable<InventorySlot>) {
    slots.forEach { slot ->
        clickListener.remember(slot)
        inventory.setItem(slot.index, slot.item)
    }
}

/** Calls [setInventorySlot] only when [predicate] returns `true`. */
public fun Menu.setInventorySlotIf(
    slot: InventorySlot,
    predicate: () -> Boolean
) {
    if (!predicate.invoke()) return
    setInventorySlot(slot)
}

/** Clears all registered click handlers and inventory items. */
public fun Menu.clear() {
    clickListener.clear()
    inventory.clear()
}
