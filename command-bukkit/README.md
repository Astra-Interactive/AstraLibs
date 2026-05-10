# Module command-bukkit

Paper-specific implementation of the [command](../command/README.md) module. Provides a concise Kotlin DSL directly over `CommandSourceStack`, Bukkit-typed argument converters, and `PaperCommandRegistrarContext` for lifecycle-safe command registration.

> **Depends on `command`** — read the [command README](../command/README.md) first to understand `ArgumentConverter`, exception types, and the error-handling pattern.

## Registering commands

Use `PaperCommandRegistrarContext` as the single place that holds registration logic. It listens for Paper's `COMMANDS` lifecycle event via coroutines, so commands are always registered at the right moment — no need to manage timing yourself.

```kotlin
class MyPlugin : JavaPlugin() {

    override fun onEnable() {
        val registrarContext = PaperCommandRegistrarContext(
            mainScope = mainScope,
            plugin = this
        )
        MyCommand(registrarContext).register()
    }

    override fun onDisable() {
        mainScope.cancel()
    }
}
```

## Command DSL

Build a command tree with `command { }`, `literal { }`, `argument { }`, and `runs { }`. These are extension functions on Paper's `LiteralArgumentBuilder<CommandSourceStack>` / `RequiredArgumentBuilder<CommandSourceStack, T>`.

### Simple command

```kotlin
class TeleportCommand(private val registrarContext: CommandRegistrarContext) {

    fun register() {
        registrarContext.registerWhenReady(
            command("tp") {
                runs(
                    onFailure = { ctx, throwable ->
                        when (throwable) {
                            is NotPlayerExecutorException ->
                                ctx.source.sender.sendMessage("Only players can teleport.")
                            is NoPlayerException ->
                                ctx.source.sender.sendMessage("Player not found.")
                            is NoPermissionException ->
                                ctx.source.sender.sendMessage("No permission.")
                        }
                    }
                ) { ctx ->
                    ctx.requirePermission(Permission("myplugin.tp"))
                    val player = ctx.requirePlayer()
                    player.teleport(player.world.spawnLocation)
                    player.sendMessage("Teleported to spawn!")
                }
            }
        )
    }
}
```

### Command with arguments

```kotlin
class GiveCommand(private val registrarContext: CommandRegistrarContext) {

    fun register() {
        registrarContext.registerWhenReady(
            command("give") {
                argument<String>("target", StringArgumentType.word()) { targetArg ->
                    argument<Int>("amount", IntegerArgumentType.integer(1, 64)) { amountArg ->
                        runs(
                            onFailure = { ctx, throwable ->
                                ctx.source.sender.sendMessage(throwable.message ?: "Error")
                            }
                        ) { ctx ->
                            ctx.requirePermission(Permission("myplugin.give"))

                            val targetName = ctx.requireArgument(targetArg)
                            val target = ctx.requireArgument(targetArg, OnlinePlayerArgumentConverter)
                            val amount = ctx.requireArgument(amountArg)

                            val stack = ItemStack(Material.DIAMOND, amount)
                            target.inventory.addItem(stack)
                            ctx.source.sender.sendMessage("Gave $amount diamonds to ${target.name}.")
                        }
                    }
                }
            }
        )
    }
}
```

### Subcommands with `literal`

```kotlin
command("myplugin") {
    literal("reload") {
        runs(onFailure = ::handleError) { ctx ->
            ctx.requirePermission(Permission("myplugin.reload"))
            // reload config...
            ctx.requirePlayer().sendMessage("Config reloaded.")
        }
    }

    literal("info") {
        runs(onFailure = ::handleError) { ctx ->
            ctx.source.sender.sendMessage("MyPlugin v1.0")
        }
    }
}
```

### Tab-completion hints

Add suggestions for string arguments with `hints { }`:

```kotlin
argument<String>("world", StringArgumentType.word()) { worldArg ->
    hints { Bukkit.getWorlds().map { it.name } }
    runs(onFailure = ::handleError) { ctx ->
        val worldName = ctx.requireArgument(worldArg)
        // ...
    }
}
```

## Built-in Bukkit converters

Pass these to `ctx.requireArgument(arg, converter)` to convert a string argument to a Bukkit type:

| Converter | Returns | Throws on failure |
|---|---|---|
| `OnlinePlayerArgumentConverter` | `Player` | `NoPlayerException` |
| `OfflinePlayerArgumentConverter` | `OfflinePlayer` | `NoPlayerException` |
| `PotionEffectTypeArgumentConverter` | `PotionEffectType` | `NoPotionEffectTypeException` |

```kotlin
argument<String>("player", StringArgumentType.word()) { playerArg ->
    runs(onFailure = ::handleError) { ctx ->
        val player: Player = ctx.requireArgument(playerArg, OnlinePlayerArgumentConverter)
        player.sendMessage("Hello!")
    }
}
```

## Context helpers

Inside `runs { ctx -> }`, these extension functions are available on `CommandContext<CommandSourceStack>`:

| Function | Returns | Throws |
|---|---|---|
| `ctx.requirePlayer()` | `Player` | `NotPlayerExecutorException` |
| `ctx.requirePermission(permission)` | `Unit` | `NoPermissionException` |
| `ctx.requireArgument(arg)` | `T` | `IllegalArgumentException` |
| `ctx.requireArgument(arg, converter)` | `T` | `ArgumentConverterException` |

## Full example: effect command

```kotlin
class GiveEffectCommand(private val registrarContext: CommandRegistrarContext) {

    fun register() {
        registrarContext.registerWhenReady(
            command("aeffect") {
                argument<String>("player", StringArgumentType.word()) { playerArg ->
                    argument<String>("effect", StringArgumentType.word()) { effectArg ->
                        argument<Int>("power", IntegerArgumentType.integer(0, 255)) { powerArg ->
                            argument<Int>("duration", IntegerArgumentType.integer(1)) { durationArg ->
                                runs(
                                    onFailure = { ctx, throwable ->
                                        val msg = when (throwable) {
                                            is NoPermissionException ->
                                                "No permission: ${throwable.permission}"
                                            is NotPlayerExecutorException ->
                                                "Only players can use this."
                                            is NoPlayerException ->
                                                "Player '${throwable.name}' is not online."
                                            is NoPotionEffectTypeException ->
                                                "Unknown effect: ${throwable.name}"
                                            else -> throwable.message ?: "An error occurred."
                                        }
                                        ctx.source.sender.sendMessage(msg)
                                    }
                                ) { ctx ->
                                    ctx.requirePermission(Permission("myplugin.effect"))

                                    val target = ctx.requireArgument(playerArg, OnlinePlayerArgumentConverter)
                                    val effect = ctx.requireArgument(effectArg, PotionEffectTypeArgumentConverter)
                                    val power = ctx.requireArgument(powerArg)
                                    val duration = ctx.requireArgument(durationArg)

                                    target.addPotionEffect(PotionEffect(effect, duration, power))
                                    ctx.source.sender.sendMessage(
                                        "Applied ${effect.name} to ${target.name}."
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}
```

## PaperMultiplatformCommands

If you need to share command definitions across server platforms (not just Paper), use `PaperMultiplatformCommands` together with the `MultiplatformCommand` DSL from the `command` module. See [command README](../command/README.md#multiplatform-command-dsl) for details.

```kotlin
val commands = PaperMultiplatformCommands()
val multiCmd = MultiplatformCommand(commands)
// use with(multiCmd) { command("...") { ... } }
```
