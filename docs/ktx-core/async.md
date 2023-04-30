## AstraLibs Coroutines package

This package allows you to handle dispatchers and coroutine scope. It contains:

### AsyncComponent

Is like ViewModel from Android - you can launch coroutines from it, you can close it

```kotlin
class MyCustomScope : AsyncComponent {
    fun launch() = componentScope.launch {
        println("I'm launched in scope: $this!")
    }
}
```

### KotlinDispatchers

It will help you to create custom dispatchers for your Minecraft plugin/library/mod

```kotlin
// Use like this to implement your own dispatchers
class MyDispatchers : KotlinDispatchers { ... }

// Or use this to have default
// KDispatchers is an object implementation
class MyClass(dispatchers: KotlinDispatchers = KDispatchers) { ... }
```

### PluginScope

This is UnsafeApi global object of AsyncComponent

```kotlin
fun launchGlobal() {
    PluginScope.launch { println("Hello World!") }
}
```

### Bukkit dispatchers

There's two ready dispatchers for Bukkit:

- BukkitAsyncDispatcher - will use ::runTaskAsynchronously
- BukkitMainDispatcher - will use ::runTask
  To use it you need to either bind DefaultBukkitDispatchers(plugin) or create your own interface of BukkitDispatchers

```kotlin
class DefaultBukkitDispatchers(plugin: Plugin) : BukkitDispatchers, KotlinDispatchers by KDispatchers {
    override val BukkitMain: CoroutineDispatcher = BukkitMainDispatcher(plugin)
    override val BukkitAsync: CoroutineDispatcher = BukkitAsyncDispatcher(plugin)
}

suspend fun call(dispatchers: BukkitDispatchers) {
    withContext(dispatchers.BukkitMain) {
        println("Hello from bukkit main thread!")
    }
} 
```