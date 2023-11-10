## Command

This package will let you handle command in MVI-style

It contains from multiple parts:

### Command\<T1,T2>

This interface will define your command structure:

```kotlin
interface GiveItemCommand : Command<GiveItemCommand.Result, GiveItemCommand.Input> {

    sealed interface Result {
        data object NoOp : Result
        data object NoPermission : Result
        data object PlayerNotExists : Result
        class Success(val material: Material, val amount: Int, val receiver: Player) : Result
    }

    class Input()
}

```

### CommandParser\<T1>

With this parser you can also create tests for result outcome! No more testing in-game for commands!

```kotlin
class GiveItemCommandParser : CommandParser<GiveItemCommand.Result> {
    // give PLAYER ITEM AMOUNT
    override fun parse(args: Array<out String>, sender: CommandSender): GiveItemCommand.Result {
        val player = args.getOrNull(0)?.let(Bukkit::getOnlinePlayer)
        player ?: return GiveItemCommand.Result.PlayerNotExists
        // other code
    }
}
```

### CommandMapper\<T1,T2>

CommandMapper will map result into Input of Executor. It's like Reducer from MVI

```kotlin
private inner class GiveItemCommandMapper : Command.Mapper<GiveItemCommand.Result, GiveItemCommand.Input> {
    override fun toInput(result: GiveItemCommand.Result): GiveItemCommand.Input? {
        return when (result) {
            is GiveItemCommand.Result.Success -> TODO()
            else -> null
        }
    }
}
```

### ResultHandler<T1>

After successful/failed parsing you will be able to send player a message

```kotlin
class GiveItemCommandResultHandler : Command.ResultHandler<GiveItemCommand.Result> {
    override fun handle(commandSender: CommandSender, result: GiveItemCommand.Result) {
        when (result) {
            TODO()
        }
    }
}
```

### CommandExecutor\<T2>

CommandExecutor will execute command with Input

```kotlin
private inner class MyCommandExecutor : CommandExecutor<SampleCommand.Input> {
    override fun execute(input: SampleCommand.Input) {
        TODO()
    }
}
```

### CommandFactory

```kotlin
class SampleCommandFactory(private val plugin: JavaPlugin) : Factory<SampleCommand> {

    override fun create(): SampleCommand {
        return SampleCommandImpl()
    }
    inner class SampleCommandImpl :
        SampleCommand,
        Command<SampleCommand.Result, SampleCommand.Input> by DefaultCommandFactory.create(
            plugin = plugin,
            alias = "sample",
            commandParser = GiveItemCommandParser(),
            commandExecutor = MyCommandExecutor(),
            resultHandler = GiveItemCommandResultHandler(),
            mapper = GiveItemCommandMapper()
        )
}
```

### Feeling overhead? Use lambdas

```kotlin
class SampleCommandFactory(private val plugin: JavaPlugin) : Factory<SampleCommand> {
    
    private fun registerTabCompleter() {
        //effect give <player> <effect>
        //effect clear <player>
        plugin.registerTabCompleter("test") {
            label(0) {
                listOf("give", "clear")
            }
            label(1) {
                Bukkit.getOnlinePlayers().map(Player::getName)
            }
            predicate(0, "give") {
                label(2) {
                    PotionEffectType.values().map { it.name }
                }
            }
            hints()
        }
    }
    
    override fun create(): SampleCommand {
        registerTabCompleter()
        return SampleCommandImpl()
    }

    inner class SampleCommandImpl :
        SampleCommand,
        Command<SampleCommand.Result, SampleCommand.Input> by DefaultCommandFactory.create(
            plugin = plugin,
            alias = "sample",
            commandParser = { args, sender ->
                SampleCommand.Result.NoOp
            },
            commandExecutor = {
            },
            resultHandler = Command.ResultHandler.NoOp(),
            mapper = {
                SampleCommand.Input()
            }
        )
}
```