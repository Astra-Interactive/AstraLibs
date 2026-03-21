package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.RemoteConsoleCommandSender
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.KCommandDispatcher

fun Player.asKCommandDispatcher() = KCommandDispatcher { command ->
    this.performCommand(command)
}

fun CommandSender.asKCommandDispatcher() = KCommandDispatcher { command ->
    when (this) {
        is RemoteConsoleCommandSender,
        is ConsoleCommandSender -> {
            Bukkit.getServer().dispatchCommand(this, command)
        }

        else -> error("Could not dispatch command from $this")
    }
}
