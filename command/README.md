# Module command

A multiplatform Brigadier command framework built around Kotlin's type system. Commands are declared with a Kotlin DSL, arguments are parsed by typed converters that throw exceptions on failure, and errors are handled in a single place — no `if`/`else` chains, no null checks.

The `command` module contains the platform-agnostic core: converters, exception types, and the multiplatform DSL. For Paper/Spigot use, see [command-bukkit](../command-bukkit/README.md) which builds on top of this module.

## Core concepts

### ArgumentConverter

`ArgumentConverter<T>` transforms a raw `String` argument into a typed value. If conversion is impossible it must throw `ArgumentConverterException` (or any subclass of `CommandException`). This is the only mechanism for argument validation — no nulls, no optionals.

```kotlin
fun interface ArgumentConverter<T : Any> {
    @Throws(ArgumentConverterException::class)
    fun transform(argument: String): T
}
```

**Built-in converters** (available as singleton objects):

| Object                     | Target type |
|----------------------------|-------------|
| `IntArgumentConverter`     | `Int`       |
| `StringArgumentConverter`  | `String`    |
| `DoubleArgumentConverter`  | `Double`    |
| `FloatArgumentConverter`   | `Float`     |
| `BooleanArgumentConverter` | `Boolean`   |

**Custom converter example:**

```kotlin
object WorldArgumentConverter : ArgumentConverter<World> {
    override fun transform(argument: String): World {
        return Bukkit.getWorld(argument)
            ?: throw ArgumentConverterException(WorldArgumentConverter::class.java, argument)
    }
}
```

### Exception types

All command exceptions extend `CommandException`. Throw them freely inside converters and command handlers — they propagate to the `onFailure` handler you provide in `runs { }`.

| Exception                                   | When to throw                      |
|---------------------------------------------|------------------------------------|
| `CommandException(message)`                 | Generic command error              |
| `StringDescCommandException(stringDesc)`    | Error with a localised description |
| `BadArgumentException(wrongArg, converter)` | Argument failed to parse           |
| `ArgumentConverterException(class, value)`  | Converter could not convert value  |
| `NoPermissionException(permission)`         | Sender lacks a permission          |
| `NoPlayerException(name)`                   | Named player not found             |
| `NotPlayerExecutorException`                | Command requires a player sender   |
| `NoPotionEffectTypeException(name)`         | Unknown potion effect name         |

### Multiplatform command DSL

`MultiplatformCommand` wraps Brigadier's generic builder types behind a platform-neutral DSL. You use it when you want one command definition that can be registered on different server platforms. For Paper-only plugins the [command-bukkit](../command-bukkit/README.md) DSL is simpler.

```kotlin
class MyPluginCommand(
    private val registrarContext: CommandRegistrarContext,
    commands: MultiplatformCommands,
) {
    private val cmd = MultiplatformCommand(commands)

    fun register() {
        val node = with(cmd) {
            command("myplugin") {
                literal("reload") {
                    runs(
                        onFailure = { ctx, throwable ->
                            val sender = ctx.getSender()
                            when (throwable) {
                                is NoPermissionException ->
                                    sender.sendMessage("No permission: ${throwable.permission}")
                                else ->
                                    sender.sendMessage("Error: ${throwable.message}")
                            }
                        }
                    ) { ctx ->
                        ctx.requirePermission(Permission("myplugin.reload"))
                        ctx.getSender().sendMessage("Reloaded!")
                    }
                }

                literal("give") {
                    argument("player", StringArgumentType.word(), BrigadierArgument("player", StringArgumentType.word(), String::class.java)) { playerArg ->
                        runs(
                            onFailure = { ctx, throwable ->
                                ctx.getSender().sendMessage(throwable.message ?: "Error")
                            }
                        ) { ctx ->
                            ctx.requirePermission(Permission("myplugin.give"))
                            val playerName = ctx.requireArgument(playerArg)
                            val player = ctx.requireArgument(playerArg, OnlineKPlayerArgumentConverter(platformServer))
                            player.sendMessage("You received an item!")
                        }
                    }
                }
            }
        }
        registrarContext.registerWhenReady(node)
    }
}
```

### CommandRegistrarContext

`CommandRegistrarContext` abstracts the moment when a command node should be registered with the server. Implementations know when that window opens (e.g. during the Brigadier lifecycle event on Paper). Call `registerWhenReady` and the implementation takes care of timing.

```kotlin
interface CommandRegistrarContext {
    fun registerWhenReady(node: LiteralArgumentBuilder<*>)
}
```

### KCommandSender

Commands resolve their sender to `KCommandSender`, a sealed interface with two implementations:

- `ConsoleKCommandSender` — console with permission checking and command dispatch
- `KPlayerKCommandSender(instance: OnlineKPlayer)` — player sender delegating to `OnlineKPlayer`

Use `ctx.getSender()` inside `runs { }` to get the sender in a platform-neutral way.

## Error handling pattern

The pattern throughout this library is: **parse optimistically, handle errors once**. Converters and helpers throw typed exceptions; `onFailure` catches them all in one place.

```kotlin
runs(
    onFailure = { ctx, throwable ->
        val msg = when (throwable) {
            is NoPermissionException -> "You lack permission: ${throwable.permission}"
            is NotPlayerExecutorException -> "Only players can use this command."
            is ArgumentConverterException -> "Invalid value '${throwable.value}'"
            is BadArgumentException -> "Bad argument: ${throwable.wrongArgument}"
            else -> throwable.message ?: "An error occurred."
        }
        ctx.getSender().sendMessage(msg)
    }
) { ctx ->
    ctx.requirePermission(Permission("myplugin.use"))
    val player = ctx.requirePlayer()          // throws NotPlayerExecutorException if sender is console
    // ... rest of command logic
}
```
