[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.astralibs/ktx-core?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)[![kotlin_version](https://img.shields.io/badge/kotlin-1.9.0-blueviolet?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)
[![spigot_version](https://img.shields.io/badge/spigot-%3E1.16-green?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)

# AstraLibs

This is a library with helpful functions for AstraInteractive plugins

As an example - you can see how it being used in [AstraTemplate](https://github.com/Astra-Interactive/AstraTemplate)

For some documentation see [docs](./docs/home.md)

### You can use AstraLibs as you want, but you are not allowed to use it in "pay to win projects"

### Installation

Firstly, add a mavenCentral repository

```kotlin
repositories {
    mavenCentral()
}
```

Next, add required repositories into `dependencies { ... }`

```kotlin
// ktx-core with basic kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:ktx-core:<version>")
// Lightweight simple ORM with kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:orm:<version>")
// Spigot-core dependencies
implementation("ru.astrainteractive.astralibs:spigot-core:<version>")
// Spigot module which focused on GUI
implementation("ru.astrainteractive.astralibs:spigot-gui:<version>")
```

Version catalogs

```toml
[versions]
astralibs = "<latest-version>"

[libraries]
astralibs-orm = { module = "ru.astrainteractive.astralibs:orm", version.ref = "astralibs" }
astralibs-ktx-core = { module = "ru.astrainteractive.astralibs:ktx-core", version.ref = "astralibs" }
astralibs-spigot-gui = { module = "ru.astrainteractive.astralibs:spigot-gui", version.ref = "astralibs" }
astralibs-spigot-core = { module = "ru.astrainteractive.astralibs:spigot-core", version.ref = "astralibs" }
```

### Command [WIP]
Create tab completer
```kotlin
//effect give <player> <effect>
//effect clear <player>
plugin.registerTabCompleter("test") {
    label(0) {
        listOf("give", "clear")
    }
    label(1) {
        Bukkit.getOnlinePlayers().map(Player::getName)
    }
    predicate(0, "give") {
        label(2) {
            PotionEffectType.values().map { it.name }
        }
    }
    hints()
}
```
Create command
```kotlin
registerCommand("test") {
    //effect give <player> <effect>
    //effect clear <player>
    label(0, "give") {
        argument(1, OnlinePlayerArgument).onResult { player ->
            argument(2, PotionEffectTypeArgument).onResult { potionEffectType ->
                // execute logic
            }.onFailure {
                sender.sendMessage("${it.rawValue} not potion effect")
            }
        }.onFailure {
            sender.sendMessage("${it.rawValue} not a player")
        }
    }
    label(0, "clear") {
        argument(1, OnlinePlayerArgument).onResult { player ->
            // execute logic
        }.onFailure {
            sender.sendMessage("${it.rawValue} not a player")
        }
    }
}
```
### Permission
```kotlin
sealed class Permissions(override val value: String) : Permission {
    data object Reload : Permissions("command.reload")
    data object Counts : Permissions("command.counts.2")
}
fun checkPerm(player: Player) {
    Permissions.Reload.hasPermission(sender) // -> bool
    Permissions.Counts.permissionSize(sender) // -> 2
}
```