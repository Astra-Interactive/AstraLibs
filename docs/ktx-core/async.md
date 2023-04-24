## AstraLibs Coroutines package
This package allows you to handle dispatchers and coroutine scope. It contains:

### AsyncComponent
Is like ViewModel from Android - you can launch coroutines from it, you can close it
```kotlin
class MyCustomScope: AsyncComponent { 
    fun launch() = componentScope.launch {
        println("I'm launched in scope: $this!")
    }
}
```
### KotlinDispatchers
It will help you to create custom dispatchers for your Minecraft plugin/library/mod
```kotlin
// Use like this to implement your own dispatchers
class MyDispatchers: KotlinDispatchers { ... }
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