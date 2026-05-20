package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.KCommandDispatcher

/** Adapts this [Player] as a [KCommandDispatcher]. */
fun Player.asKCommandDispatcher() = KCommandDispatcher { command ->
    this.performCommand(command)
}

/**
 * Adapts this [CommandSender] as a [KCommandDispatcher].
 * Only [ConsoleCommandSender] and [RemoteConsoleCommandSender] are supported.
 *
 * @throws IllegalStateException if this sender is neither a console nor a remote console.
 */
fun CommandSender.asKCommandDispatcher() = KCommandDispatcher { command ->
    when (this) {
        is RemoteConsoleCommandSender,
        is ConsoleCommandSender -> {
            Bukkit.getServer().dispatchCommand(this, command)
        }

        else -> error("Could not dispatch command from $this")
    }
}
