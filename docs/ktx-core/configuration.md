## Configuration

Configuration is a safe key-value storage interface. It can be Mutable or Immutable

Let's see how to create simple In-Memory Configuration

```kotlin
class InMemoryConfiguration<T>(initialValue: T) : Configuration<T> {
    override val value: T = initialValue

    override fun loadValue(): T {
        throw Exception("In memory value cannot be loaded!")
    }
}

fun func() {
    val configuration = InMemoryConfiguration(10)
}
```

## Mutable Configuration

But what if we want it to be mutable?

```kotlin
class InMemoryConfiguration<T>(private val initialValue: T) : MutableConfiguration<T> {
    override var value: T = initialValue

    override fun loadValue(): T {
        throw Exception("In memory value cannot be loaded!")
    }

    override fun reset() {
        value = initialValue
    }

    override fun saveValue(block: (T) -> T) {
        saveValue(block)
    }

    override fun saveValue(value: T) {
        this.value = value
    }
}

fun function() {
    val configuration = InMemoryConfiguration(10)
    configuration.value = 100
    configuration.reset()
}
```

## Bukkit configurations

Well, that's awesome, but how about Bukkit configurations?

Good news! It's already implemented. Configurations for bukkit can be created easily:

```kotlin
// Use this to create your own configuration
class CustomIntConfiguration(
    fileConfiguration: FileConfiguration,
    default: Int,
    path: String,
) : MutableConfiguration<Int> by DefaultConfiguration(
    default = default,
    save = { fileConfiguration.set(path, value) },
    load = { fileConfiguration.getInt(path, default) }
)

// Or use existed one for Any configuration
fun customAnyConfiguration(fc: FileConfiguration) {
    val intValueConfiguration = AnyConfiguration(fc, "config.int_value", 0)
    intValueConfiguration.saveValue(15)
}
```