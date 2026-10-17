# Module menu-bukkit

A Bukkit/Paper inventory GUI framework built on Kotlin coroutines. Create chest-style menus with typed slots, click handling, semantic layouts, and pagination — lifecycle is tied to coroutine scopes so cleanup is automatic when the player closes the inventory.

## Installation

```kotlin
dependencies {
    implementation("ru.astrainteractive.astralibs:menu-bukkit:<version>")
}
```

## Setup

Register `DefaultInventoryClickEvent` once when your plugin enables. It routes all Bukkit inventory events to the right `Menu` instance.

```kotlin
class MyPlugin : JavaPlugin() {
    override fun onEnable() {
        DefaultInventoryClickEvent().register(this)
    }
}
```

## Basic menu

Extend `InventoryMenu`, declare size and title, then place slots inside `render()`.

```kotlin
class ShopMenu(player: Player) : InventoryMenu() {

    override val title: Component = Component.text("Shop")
    override val inventorySize: InventorySize = InventorySize.M   // 36 slots (4 rows)
    override val menuScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    override val childComponents: List<CoroutineScope> = emptyList()

    override fun onInventoryOpenEvent(e: InventoryOpenEvent) {
        render()
    }

    override fun render() {
        clear()
        setInventorySlot(
            InventorySlot.Builder()
                .setIndex(13)
                .setMaterial(Material.DIAMOND)
                .setDisplayName(Component.text("Buy Diamond — $100"))
                .setOnClickListener { e ->
                    e.whoClicked.sendMessage("Purchased!")
                    e.whoClicked.closeInventory()
                }
                .build()
        )
    }
}

// Open it for a player:
val menu = ShopMenu(player)
player.openInventory(menu.getInventory())
```

`menuScope` is cancelled automatically when the player closes the inventory, so any coroutines you launch inside it stop cleanly without extra bookkeeping.

### Inventory sizes

| Value               | Slots | Rows |
|---------------------|-------|------|
| `InventorySize.XXS` | 9     | 1    |
| `InventorySize.XS`  | 18    | 2    |
| `InventorySize.S`   | 27    | 3    |
| `InventorySize.M`   | 36    | 4    |
| `InventorySize.L`   | 45    | 5    |
| `InventorySize.XL`  | 54    | 6    |

## InventorySlot builder

All slot properties are set through the builder:

```kotlin
InventorySlot.Builder()
    .setIndex(22)
    .setMaterial(Material.EMERALD)
    .setAmount(3)
    .setDisplayName(Component.text("My Item"))
    .setLore(
        listOf(
            Component.text("Line 1"),
            Component.text("Line 2")
        )
    )
    .editMeta { isUnbreakable = true }     // mutate ItemMeta directly
    .setOnClickListener { event ->
        event.whoClicked.sendMessage("Clicked!")
    }
    .build()
```

Use `predicate` to apply builder changes conditionally without breaking the chain:

```kotlin
InventorySlot.Builder()
    .setMaterial(Material.STONE)
    .predicate(player.isOp) {
        setMaterial(Material.GOLD_BLOCK)
        setDisplayName(Component.text("Admin-only item"))
    }
    .build()
```

Use `setInventorySlotIf` on the menu to skip drawing a slot entirely when a condition is false:

```kotlin
setInventorySlotIf(
    slot = InventorySlot.Builder()
        .setIndex(0)
        .setMaterial(Material.BARRIER)
        .setDisplayName(Component.text("Previous page"))
        .setOnClickListener { /* go back */ }
        .build(),
    predicate = { currentPage > 0 }
)
```

## Semantic layouts

`InventoryLayout` maps symbolic keys to slot indices, eliminating hardcoded numbers spread across `render()`.

```kotlin
enum class Zone { BORDER, CONTENT, PREV, CLOSE, NEXT }

val layout: InventoryLayout<Zone, InventorySlot> = inventoryLayout {
    row(9, Zone.BORDER)                                           // row 1 — all border
    row(
        Zone.BORDER, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT,
        Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.BORDER
    )
    row(
        Zone.BORDER, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT,
        Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.BORDER
    )
    row(
        Zone.BORDER, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT,
        Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.BORDER
    )
    row(
        Zone.BORDER, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT,
        Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.CONTENT, Zone.BORDER
    )
    row(
        Zone.PREV, Zone.BORDER, Zone.BORDER, Zone.BORDER,
        Zone.CLOSE, Zone.BORDER, Zone.BORDER, Zone.BORDER, Zone.NEXT
    )
}

// In render():
layout.indicesOf(Zone.BORDER).forEach { index ->
    setInventorySlot(
        InventorySlot.Builder()
            .setIndex(index)
            .setMaterial(Material.GRAY_STAINED_GLASS_PANE)
            .setDisplayName(Component.empty())
            .build()
    )
}

val closeSlotIndex = layout.firstIndexOf(Zone.CLOSE)
val contentSlotCount = layout.count(Zone.CONTENT)   // number of content slots
```

## Pagination

`DefaultPaginator` holds reactive page state via `StateFlow`. Collect it in `menuScope` and call `render()` on every change.

```kotlin
class ItemListMenu(
    private val items: List<String>,
    player: Player
) : InventoryMenu() {

    override val title: Component = Component.text("Items (${items.size})")
    override val inventorySize: InventorySize = InventorySize.XL  // 54 slots
    override val menuScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    override val childComponents: List<CoroutineScope> = emptyList()

    private val paginator = DefaultPaginator(
        maxItems = items.size,
        maxItemsPerPage = 45                    // slots 0–44 for items
    )

    override fun onInventoryOpenEvent(e: InventoryOpenEvent) {
        menuScope.launch {
            paginator.paginatorContextStateFlow.collect { render() }
        }
    }

    override fun render() {
        clear()
        val ctx = paginator.context

        // Draw items for the current page
        items
            .drop(ctx.page * ctx.maxItemsPerPage)
            .take(ctx.maxItemsPerPage)
            .forEachIndexed { i, name ->
                setInventorySlot(
                    InventorySlot.Builder()
                        .setIndex(i)
                        .setMaterial(Material.PAPER)
                        .setDisplayName(Component.text(name))
                        .build()
                )
            }

        // Previous page button — only shown when not on first page
        setInventorySlotIf(
            slot = InventorySlot.Builder()
                .setIndex(45)
                .setMaterial(Material.ARROW)
                .setDisplayName(Component.text("← Previous"))
                .setOnClickListener { paginator.openPrevPage() }
                .build(),
            predicate = { !ctx.isFirstPage }
        )

        // Next page button — only shown when not on last page
        setInventorySlotIf(
            slot = InventorySlot.Builder()
                .setIndex(53)
                .setMaterial(Material.ARROW)
                .setDisplayName(Component.text("Next →"))
                .setOnClickListener { paginator.openNextPage() }
                .build(),
            predicate = { !ctx.isLastPage }
        )
    }
}

// Open it:
val menu = ItemListMenu(items = listOf("Sword", "Shield", "Potion"), player = player)
player.openInventory(menu.getInventory())
```

### PaginatorContext helpers

| Property / Function        | Description                                             |
|----------------------------|---------------------------------------------------------|
| `ctx.page`                 | Zero-based current page index                           |
| `ctx.maxPages`             | Total number of pages                                   |
| `ctx.isFirstPage`          | True when on page 0                                     |
| `ctx.isLastPage`           | True when on the last page                              |
| `ctx.indexOfSlot(i)`       | Convert a within-page slot index to absolute list index |
| `paginator.openNextPage()` | Navigate to the next page                               |
| `paginator.openPrevPage()` | Navigate to the previous page                           |
| `paginator.setMaxItems(n)` | Update total item count (e.g. after filtering)          |

## Coroutine-driven menus

Because `menuScope` is a regular `CoroutineScope`, you can load data asynchronously and re-render when it arrives:

```kotlin
override fun onInventoryOpenEvent(e: InventoryOpenEvent) {
    menuScope.launch {
        val data = database.loadItems()          // suspend call, off main thread
        withContext(Dispatchers.Main) {
            items = data
            render()
        }
    }
}
```
