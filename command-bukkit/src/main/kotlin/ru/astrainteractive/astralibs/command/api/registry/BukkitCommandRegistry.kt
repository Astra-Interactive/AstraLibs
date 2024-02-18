package ru.astrainteractive.astralibs.command.api.registry

import ru.astrainteractive.astralibs.command.api.command.Command
import ru.astrainteractive.astralibs.command.api.context.BukkitCommandContext

object BukkitCommandRegistry : CommandRegistry<BukkitCommandRegistryContext, Command<BukkitCommandContext>> {
    override fun register(command: Command<BukkitCommandContext>, registryContext: BukkitCommandRegistryContext) {
        registryContext.javaPlugin.getCommand(command.alias)?.setExecutor { sender, bukkitCommand, label, args ->
            val commandContext = BukkitCommandContext(
                sender = sender,
                command = bukkitCommand,
                label = label,
                args = args
            )
            command.dispatch(commandContext)
            true
        }
    }
}
