# Add as dependency

To add AstraLibs into your module you will need to add repository:
```kotlin
repositories {
    mavenCentral()
}
```

Next, add required repositories into `dependencies { ... }`

```groovy
// ktx-core with basic kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:ktx-core:<version>")
// Manual DI with kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:di:<version>")
// Lightweight simple ORM with kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:orm:<version>")
// Spigot-core dependencies
implementation("ru.astrainteractive.astralibs:spigot-core:<version>")
// Spigot module which focused on GUI
implementation("ru.astrainteractive.astralibs:spigot-gui:<version>")
```

That's it! Now you can use AstraLibs