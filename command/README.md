#### [Home README](../README.md)

# MVI-style Command

This module will help you create testable command implementation with Parser, SideEffect and Executor.

This separation of responsibilities will guarantee you testability and stability of your code.

## Creating Reload command

```kotlin
/**
 * Declare a registry for your new command
 */
class ReloadCommandRegistry(private val plugin: AstraTemplate) {
    /**
     * Declare a structure of your command
     */
    interface ReloadCommand : BukkitCommand {
        /**
         * When player executes command - there's two options
         * Failure - player doesn't have permission
         * Success - plugin will be reloaded
         */
        sealed interface Result {
            data object NoPermission : Result
            class Success(val sender: CommandSender) : Result
        }

    }

    /**
     * Command parser will parse command from context into [ReloadCommand.Result]
     */
    private inner class CommandParserImpl : BukkitCommandParser<ReloadCommand.Result> {
        override fun parse(commandContext: BukkitCommandContext): ReloadCommand.Result {
            val hasPermission = commandContext.sender.toPermissible().hasPermission(PluginPermission.Damage)
            if (!hasPermission) return ReloadCommand.Result.NoPermission
            return ReloadCommand.Result.Success(commandContext.sender)
        }
    }

    /**
     * SideEffect is used to tell executor about command execution
     */
    private inner class CommandSideEffectImpl : BukkitCommandSideEffect<ReloadCommand.Result> {
        override fun handle(commandContext: BukkitCommandContext, result: ReloadCommand.Result) {
            when (result) {
                ReloadCommand.Result.NoPermission -> {
                    commandContext.sender.sendMessage("No permission")
                }

                is ReloadCommand.Result.Success -> Unit
            }
        }
    }

    /**
     * Executor is required to execute command depending on [ReloadCommand.Result]
     */
    private inner class CommandExecutorImpl : CommandExecutor<ReloadCommand.Result> {
        override fun execute(input: ReloadCommand.Result) {
            if (input !is ReloadCommand.Result.Success) return
            input.sender.sendMessage("Plugin reload started...")
            plugin.reloadPlugin()
            input.sender.sendMessage("Plugin reloaded!")
        }
    }

    /**
     * Don't forget to register command
     */
    fun register() {
        val command = BukkitCommandFactory.create(
            alias = "reload",
            commandExecutor = CommandExecutorImpl(),
            commandSideEffect = CommandSideEffectImpl(),
            commandParser = CommandParserImpl(),
            mapper = Command.Mapper.NoOp()
        )
        BukkitCommandRegistry.register(
            command = command,
            registryContext = BukkitCommandRegistryContext(plugin)
        )
    }
}
```

## ArgumentType

Want to use safer arguments? Take a look at ArgumentType

```kotlin
/**
 * Let's see it on example of PotionEffect
 *
 * It can be parsed from string in straightforward way
 */
data object PotionEffectTypeArgument : ArgumentType<PotionEffectType?> {
    override fun transform(value: String?): PotionEffectType? {
        return value?.let(PotionEffectType::getByName)
    }
}
```

But isn't it nullable? Yes, and you can set a default value with an extension, see below

```kotlin

private inner class CommandParserImpl : BukkitCommandParser<ReloadCommand.Result> {
    override fun parse(commandContext: BukkitCommandContext): ReloadCommand.Result {
        PotionEffectTypeArgument
            .withDefault(PotionEffectType.HARM)
            .transform(commandContext.args.getOrNull(1))

        // Or just use
        PotionEffectTypeArgument
            .transform(commandContext.args.getOrNull(1))
            ?: PotionEffectType.HARM
    }
}
```

# Non-Bukkit Command implementation

## Creating custom CommandContext

Command module is scalable, so you can use command in any architecture. Let's see with velocity.

```kotlin
/**
 * When creating SimpleCommand of velocity, we have this parameters
 */
class VelocityCommandContext(
    val alias: String,
    val source: CommandSource,
    val arguments: Array<String>
) : CommandContext

/**
 * Also to register command, we need to declare custom Velocity context
 * @param plugin is our Velocity plugin
 */
class VelocityCommandRegistryContext(
    val proxyServer: ProxyServer,
    val plugin: AstraTemplate
) : CommandRegistryContext
```

## Creating CommandRegistry

CommandRegistry is required to register command on our server

```kotlin
object VelocityCommandRegistry : CommandRegistry<VelocityCommandRegistryContext, Command<VelocityCommandContext>> {
    override fun register(command: Command<VelocityCommandContext>, registryContext: VelocityCommandRegistryContext) {
        val commandMeta = registryContext.proxyServer.commandManager
            .metaBuilder(command.alias)
            .aliases(command.alias)
            .plugin(registryContext.plugin)
            .build()
        // Here we're creating SimpleCommand and declaring VelocityCommandContext
        val velocityCommand = SimpleCommand {
            val commandContext = VelocityCommandContext(
                alias = it.alias(),
                source = it.source(),
                arguments = it.arguments()
            )
            command.dispatch(commandContext)
        }
        registryContext.proxyServer.commandManager.register(commandMeta, velocityCommand)
    }
}
```

## Registering VelocityCommand

```kotlin
class ReloadCommandRegistry(private val registryContext: VelocityCommandRegistryContext) {
    fun register() {
        val command = DefaultCommandFactory<VelocityCommandContext>().create(
            alias = "reload",
            commandParser = CommandParserImpl(),
            commandExecutor = CommandExecutorImpl(),
            commandSideEffect = SideEffectImpl(),
            mapper = Command.Mapper.NoOp()
        )
        VelocityCommandRegistry.register(command, registryContext)
    }
}
```
