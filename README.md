[![Maven Central](https://img.shields.io/maven-central/v/ru.astrainteractive.astralibs/core?style=flat-square)](https://central.sonatype.com/artifact/ru.astrainteractive.astralibs/core)

# AstraLibs

**Stop writing the same Minecraft plugin boilerplate. Again.**

AstraLibs is a Kotlin-first toolkit that handles the repetitive parts of plugin and mod development - so you focus on what makes your plugin unique, not on wiring up commands, GUIs, and lifecycle hooks for the hundredth time.

Used in production across [EmpireProjekt.ru](https://www.EmpireProjekt.ru) plugins.

---

## Why AstraLibs?

**Commands without the noise.**
Declare full Brigadier command trees in a clean Kotlin DSL. Arguments are parsed by typed converters; errors bubble up to a single handler. No null checks, no `if`/`else` forests, no boilerplate. Just your logic. - [command docs](./command/README.md) · [command-bukkit docs](./command-bukkit/README.md)

**Inventory GUIs that don't leak.**
Build chest-style menus with a fluent slot builder, semantic layout mapping, and reactive pagination driven by `StateFlow`. The menu's coroutine scope is tied to its lifecycle and cancelled the moment the player closes it - zero manual cleanup. - [menu-bukkit docs](./menu-bukkit/README.md)

**One codebase, multiple platforms.**
Core abstractions - lifecycle, economy, players, events - work identically on Paper, NeoForge, and Fabric. Swap the platform module, keep your logic untouched.

**Kotlin all the way down.**
Coroutine dispatchers, `Flow`-based event streams, extension-function DSLs. No Java compatibility layers, no compromises on the API surface.

---

## Documentation

For documentation you can open specific module

- [menu-bukkit](./menu-bukkit/README.md)
- [command](./command/README.md)
- [command-bukit](./command-bukkit/README.md)

---

## Modules

| Module           | Platform | Description                                                             |
|------------------|----------|-------------------------------------------------------------------------|
| `core`           | Any      | Lifecycle, StringDesc, EconomyFacade, serialization utilities           |
| `core-bukkit`    | Paper    | BukkitDispatchers, FlowEvent, VaultEconomy, PlaceholderAPI              |
| `core-neoforge`  | NeoForge | ForgeDispatchers, NeoForge event bindings, KPlayer adapter              |
| `core-fabric`    | Fabric   | FabricDispatchers, Fabric command registration                          |
| `command`        | Any      | Brigadier DSL core, typed argument converters, exception-driven parsing |
| `command-bukkit` | Paper    | Paper command registration, Bukkit argument converters                  |
| `menu-bukkit`    | Paper    | Inventory GUI framework with layouts and pagination                     |

---

## Installation

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("ru.astrainteractive.astralibs:core:<version>")
    implementation("ru.astrainteractive.astralibs:core-bukkit:<version>")
    implementation("ru.astrainteractive.astralibs:command:<version>")
    implementation("ru.astrainteractive.astralibs:command-bukkit:<version>")
    implementation("ru.astrainteractive.astralibs:menu-bukkit:<version>")
}
```

### Version catalog

```toml
[versions]
astralibs = "<latest-version>"

[libraries]
astralibs-core = { module = "ru.astrainteractive.astralibs:core", version.ref = "astralibs" }
astralibs-core-bukkit = { module = "ru.astrainteractive.astralibs:core-bukkit", version.ref = "astralibs" }
astralibs-core-neoforge = { module = "ru.astrainteractive.astralibs:core-neoforge", version.ref = "astralibs" }
astralibs-core-fabric = { module = "ru.astrainteractive.astralibs:core-fabric", version.ref = "astralibs" }
astralibs-command = { module = "ru.astrainteractive.astralibs:command", version.ref = "astralibs" }
astralibs-command-bukkit = { module = "ru.astrainteractive.astralibs:command-bukkit", version.ref = "astralibs" }
astralibs-menu-bukkit = { module = "ru.astrainteractive.astralibs:menu-bukkit", version.ref = "astralibs" }
```

---

## Example project

- [AstraTemplate](https://github.com/Astra-Interactive/AstraTemplate) - a multi-platform (Velocity / Fabric / Bukkit) plugin template built entirely on AstraLibs. Good starting point for any new plugin.
- [AstraMarket](https://github.com/Astra-Interactive/AstraAuctions)
- [MessageBridge](https://github.com/Astra-Interactive/TelegramBridge)
- [AstraRating](https://github.com/Astra-Interactive/AstraRating)
- [SoulKeeper](https://github.com/Astra-Interactive/SoulKeeper)
- [AspeKt](https://github.com/Astra-Interactive/AspeKt)

---

> Free to use in any project except pay-to-win servers.
