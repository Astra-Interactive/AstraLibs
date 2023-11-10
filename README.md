[![version](https://img.shields.io/maven-central/v/ru.astrainteractive.astralibs/ktx-core?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)[![kotlin_version](https://img.shields.io/badge/kotlin-1.9.0-blueviolet?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)
[![spigot_version](https://img.shields.io/badge/spigot-%3E1.16-green?style=flat-square)](https://github.com/Astra-Interactive/AstraLibs)

# AstraLibs

This is a library with helpful functions for AstraInteractive plugins

As an example - you can see how it being used in [AstraTemplate](https://github.com/Astra-Interactive/AstraTemplate)

For some documentation see [docs](./docs/home.md)

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
// ktx-core with basic kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:ktx-core:<version>")
// Lightweight simple ORM with kotlin-only dependencies
implementation("ru.astrainteractive.astralibs:orm:<version>")
// Spigot-core dependencies
implementation("ru.astrainteractive.astralibs:spigot-core:<version>")
// Spigot module which focused on GUI
implementation("ru.astrainteractive.astralibs:spigot-gui:<version>")
```

Version catalogs

```toml
[versions]
astralibs = "<latest-version>"

[libraries]
astralibs-orm = { module = "ru.astrainteractive.astralibs:orm", version.ref = "astralibs" }
astralibs-ktx-core = { module = "ru.astrainteractive.astralibs:ktx-core", version.ref = "astralibs" }
astralibs-spigot-gui = { module = "ru.astrainteractive.astralibs:spigot-gui", version.ref = "astralibs" }
astralibs-spigot-core = { module = "ru.astrainteractive.astralibs:spigot-core", version.ref = "astralibs" }
```

### Command [WIP]

Create tab completer

```kotlin
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
```

Create command

```kotlin
interface SampleCommand : Command<SampleCommand.Result, SampleCommand.Input> {

    sealed interface Result {
        data object NoOp : Result
        data object NoPermission : Result
        data object PlayerNotExists : Result
        data object Success : Result
    }

    class Input()
}

class SampleCommandFactory(private val plugin: JavaPlugin) : Factory<SampleCommand> {

    override fun create(): SampleCommand {
        return SampleCommandImpl()
    }

    private inner class MyCommandExecutor : CommandExecutor<SampleCommand.Input> {
        override fun execute(input: SampleCommand.Input) {
            TODO()
        }
    }

    private inner class MyCommandParser : CommandParser<SampleCommand.Result> {
        override fun parse(args: Array<out String>, sender: CommandSender): SampleCommand.Result {
            TODO("Not yet implemented")
        }
    }

    private inner class MyCommandMapper : Command.Mapper<SampleCommand.Result, SampleCommand.Input> {
        override fun toInput(result: SampleCommand.Result): SampleCommand.Input? {
            TODO("Not yet implemented")
        }
    }

    inner class SampleCommandImpl :
        SampleCommand,
        Command<SampleCommand.Result, SampleCommand.Input> by DefaultCommandFactory.create(
            plugin = plugin,
            alias = "damage",
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

### Permission

```kotlin
sealed class Permissions(override val value: String) : Permission {
    data object Reload : Permissions("command.reload")
    data object Counts : Permissions("command.counts.2")
}

fun checkPerm(player: Player) {
    val permissible = player.toPermissible()
    permissible.hasPermission(sender) // -> bool
    permissible.permissionSize(sender) // -> 2
}
```