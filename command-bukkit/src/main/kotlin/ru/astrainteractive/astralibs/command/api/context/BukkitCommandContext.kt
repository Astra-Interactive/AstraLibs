package ru.astrainteractive.astralibs.command.api.context

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BukkitCommandContext(
    val sender: CommandSender,
    val command: Command,
    val label: String,
    val args: Array<out String>
) : CommandContext
