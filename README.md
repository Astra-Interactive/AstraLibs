[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.astralibs/core?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)[![kotlin_version](https://img.shields.io/badge/kotlin-1.9.0-blueviolet?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)
[![spigot_version](https://img.shields.io/badge/spigot-%3E1.16-green?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)

# AstraLibs

This is a library for AstraInteractive plugins

As an example - you can see how it being used in [AstraTemplate](https://github.com/Astra-Interactive/AstraTemplate)

# Documentation

For documentation see [docs](./docs/home.md)

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
// core with basic kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:core:<version>")
// Lightweight simple ORM with kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:orm:<version>")
// core-bukkit dependencies
implementation("ru.astrainteractive.astralibs:core-bukkit:<version>")
// Spigot module which focused on GUI
implementation("ru.astrainteractive.astralibs:menu-bukkit:<version>")
// Or with version catalogs
implementation(libs.astralibs.core)
implementation(libs.astralibs.orm)
implementation(libs.astralibs.menu.bukkit)
implementation(libs.astralibs.core.bukkit)
```

Version catalogs

```toml
[versions]
astralibs = "<latest-version>"

[libraries]
astralibs-orm = { module = "ru.astrainteractive.astralibs:orm", version.ref = "astralibs" }
astralibs-core = { module = "ru.astrainteractive.astralibs:core", version.ref = "astralibs" }
astralibs-menu-bukkit = { module = "ru.astrainteractive.astralibs:menu-bukkit", version.ref = "astralibs" }
astralibs-core-bukkit = { module = "ru.astrainteractive.astralibs:core-bukkit", version.ref = "astralibs" }
```