# Exception-driven commands

The command module contains:

- ArgumentType - with this you will convert your `string` into `T`. If can't = throw exception
- CommandContext - Spigot, Forge, Velocity has different inputs for commands, so this is required
- CommandParser - parse your command arguments into your custom model and throw exception if not possible
- ErrorHandler - handle any errors during parsing and send message to sender
- CommandExecutor - execute your command after successful parse

## Look at this deadly simple sample

**Zero** if-else operators during parsing. Just throw exception and handle it inside ErrorHandler!

```kotlin
class GiveEffectCommand(private val plugin: JavaPlugin) {

    private class Model(
        val player: Player,
        val effect: PotionEffectType,
        val power: Int,
        val duration: Int
    )


    fun register() {
        plugin.registerCommand(
            alias = "aeffect",
            commandParser = CommandParser { commandContext ->
                Model(
                    player = commandContext.requireArgument(0, OnlinePlayerArgument),
                    effect = commandContext.requireArgument(
                        index = 1,
                        type = ArgumentType.Lambda("PotionEffectType") { value ->
                            PotionEffectType.getByName(value) ?: throw NoPotionEffectTypeException(value)
                        },
                    ),
                    power = commandContext.argumentOrElse(2, PrimitiveArgumentType.Int) { 1 },
                    duration = commandContext.argumentOrElse(3, PrimitiveArgumentType.Int) { 1 }
                )
            },
            commandExecutor = CommandExecutor { model ->
                val potionEffect = PotionEffect(
                    model.effect,
                    model.duration,
                    model.power,
                )
                model.player.addPotionEffect(potionEffect)
            },
            errorHandler = ErrorHandler { commandContext, throwable ->
                when (val error = throwable) {
                    is BadArgumentException -> commandContext.sender.sendMessage("Bad argument: ${error.wrongArgument}")
                    is NoPermissionException -> commandContext.sender.sendMessage("No permission: ${error.permission}")
                    is NoPotionEffectTypeException -> commandContext.sender.sendMessage("No effect nbamed ${error.name}")
                }
            }
        )
    }
}
```