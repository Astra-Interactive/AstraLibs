package ru.astrainteractive.astralibs.menu.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import net.kyori.adventure.text.Component
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.inventory.InventoryHolder
import ru.astrainteractive.astralibs.coroutines.withTimings
import ru.astrainteractive.astralibs.menu.clicker.ClickListener
import ru.astrainteractive.astralibs.menu.clicker.remember
import ru.astrainteractive.astralibs.menu.event.DefaultInventoryClickEvent
import ru.astrainteractive.astralibs.menu.holder.PlayerHolder
import ru.astrainteractive.astralibs.menu.slot.InventorySlot
import ru.astrainteractive.klibs.mikro.core.coroutines.CoroutineFeature

/**
 * Contract of an interactive Bukkit menu.
 *
 * Implementers describe *what* the menu owns ([playerHolder], [title], [clickListener],
 * [menuScope], [childComponents], [getInventory]); the interface itself encodes the
 * default *lifecycle* (Template Method pattern) — opening, click dispatching, rendering
 * and closing — through interface default methods.
 *
 * To make Bukkit events reach the menu, register a [DefaultInventoryClickEvent] listener
 * once during plugin startup.
 */
abstract class Menu : InventoryHolder {
    /**
     * Owner of the menu. The [PlayerHolder.player] is the viewer for whom the inventory is opened.
     */
    abstract val playerHolder: PlayerHolder

    /**
     * Title rendered at the top of the inventory. Implementers usually pass it to
     * `Bukkit.createInventory` when constructing [getInventory].
     */
    abstract val title: Component

    /**
     * Click dispatcher for this menu. Each rendered [InventorySlot] is registered here so that
     * incoming clicks are routed to the right handler.
     */
    abstract val clickListener: ClickListener

    /**
     * [Dispatchers.IO] Coroutine scope tied to the menu's visible lifetime. It is cancelled in
     * [onInventoryCloseEvent], so any work launched in it is cleaned up automatically.
     */
    open val menuScope: CoroutineScope = CoroutineFeature.IO.withTimings()

    /**
     * Auxiliary scopes (e.g. paginators, async loaders) whose lifetime should follow the
     * menu's. They are canceled together with [menuScope] in [onInventoryCloseEvent].
     */
    abstract val childComponents: List<CoroutineScope>

    /**
     * Default Bukkit click handler — delegates to [clickListener]. Override only if you
     * need to add behavior *around* the standard slot dispatch.
     */
    open fun onInventoryClickEvent(e: InventoryClickEvent) {
        clickListener.onClick(e)
    }

    /**
     * Default close handler — clears registered click handlers and cancels every
     * coroutine scope owned by the menu. Override responsibly: always call `super` to
     * preserve cleanup semantics.
     */
    open fun onInventoryCloseEvent(e: InventoryCloseEvent) {
        clickListener.clear()
        menuScope.cancel()
        childComponents.forEach(CoroutineScope::cancel)
    }

    /**
     * Render your view, probably after [onInventoryOpenEvent]
     */
    open fun render() {
        clear()
    }

    /**
     * Hook invoked right after the inventory is opened to the player. Implementers
     * typically build the initial UI here
     */
    abstract fun onInventoryOpenEvent(e: InventoryOpenEvent)
}

/**
 * Registers this slot's [InventorySlot.click] handler in the given [Menu] and writes
 * the [InventorySlot.item] into [InventorySlot.index].
 *
 * Equivalent to a "remember + draw" operation: after this call the slot is both
 * visually present and clickable.
 */
fun Menu.setInventorySlot(slot: InventorySlot) {
    clickListener.remember(slot)
    inventory.setItem(slot.index, slot.item)
}

fun Menu.setInventorySlot(slots: Iterable<InventorySlot>) {
    slots.forEach { slot ->
        clickListener.remember(slot)
        inventory.setItem(slot.index, slot.item)
    }
}

/**
 * Conditional variant of [setInventorySlot]. When [predicate] returns `false`, the slot
 * is left untouched in [Menu] — neither the click handler nor the item is written.
 */
fun Menu.setInventorySlotIf(
    slot: InventorySlot,
    predicate: () -> Boolean
) {
    if (!predicate.invoke()) return
    setInventorySlot(slot)
}

/**
 * Resets the visual state of the menu so it can be re-built from scratch. Clears
 * both registered click handlers and inventory items.
 */
fun Menu.clear() {
    clickListener.clear()
    inventory.clear()
}

/**
 * Open inventory method for Menu class
 * Should be executed on main thread
 */
fun Menu.open() {
    playerHolder.player.openInventory(inventory)
}
