[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.astralibs/core?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)[![kotlin_version](https://img.shields.io/badge/kotlin-1.9.0-blueviolet?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)

# AstraLibs
This is a library for [EmpireProjekt.ru](https://www.EmpireProjekt.ru) plugins

### You can use AstraLibs as you want, but you are not allowed to use it in "pay to win projects"



## Documentatinon

Still in WIP, see some

- [Command](./command/README.md)

## Example

You can see how it being used
in [AstraTemplate - Velocity/Fabric/Bukkit Plugin Tempalte](https://github.com/Astra-Interactive/AstraTemplate)

## Installation

Firstly, add a mavenCentral repository

```kotlin
repositories {
    mavenCentral()
}
```

Next, add required repositories into `dependencies { ... }`

```kotlin
// core with basic kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:core:<version>")
// core-bukkit dependencies
implementation("ru.astrainteractive.astralibs:core-bukkit:<version>")
// Spigot module which focused on GUI
implementation("ru.astrainteractive.astralibs:menu-bukkit:<version>")
// Exposed integration
implementation("ru.astrainteractive.astralibs:exposed:<version>")
// Multiplatform module with command
implementation("ru.astrainteractive.astralibs:command:<version>")
implementation("ru.astrainteractive.astralibs:command-bukkit:<version>")

// Or with version catalogs
implementation(libs.astralibs.core)
implementation(libs.astralibs.core.bukkit)
implementation(libs.astralibs.exposed)
implementation(libs.astralibs.menu.bukkit)
implementation(libs.astralibs.command)
implementation(libs.astralibs.command.bukkit)
```

Version catalogs

```toml
[versions]
astralibs = "<latest-version>"

[libraries]
astralibs-exposed = { module = "ru.astrainteractive.astralibs:exposed", version.ref = "astralibs" }
astralibs-core = { module = "ru.astrainteractive.astralibs:core", version.ref = "astralibs" }
astralibs-core-bukkit = { module = "ru.astrainteractive.astralibs:core-bukkit", version.ref = "astralibs" }
astralibs-menu-bukkit = { module = "ru.astrainteractive.astralibs:menu-bukkit", version.ref = "astralibs" }
astralibs-command = { module = "ru.astrainteractive.astralibs:command", version.ref = "astralibs" }
astralibs-command-bukkit = { module = "ru.astrainteractive.astralibs:command-bukkit", version.ref = "astralibs" }
```

