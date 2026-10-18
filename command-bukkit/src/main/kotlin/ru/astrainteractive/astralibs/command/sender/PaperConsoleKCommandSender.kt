package ru.astrainteractive.astralibs.command.sender

import org.bukkit.command.CommandSender
import ru.astrainteractive.astralibs.command.sender.model.ConsoleKCommandSender
import ru.astrainteractive.astralibs.server.api.KAudience
import ru.astrainteractive.astralibs.server.api.KCommandDispatcher
import ru.astrainteractive.astralibs.server.permission.api.KPermissible
import ru.astrainteractive.astralibs.server.util.asKAudience
import ru.astrainteractive.astralibs.server.util.asKCommandDispatcher
import ru.astrainteractive.astralibs.server.util.asKPermissible

/**
 * Paper implementation of [ConsoleKCommandSender] backed by a Bukkit [CommandSender].
 *
 * Delegates [KPermissible], [KAudience], and [KCommandDispatcher] capabilities to the
 * corresponding Bukkit extension adapters, exposing a unified console-sender abstraction
 * for both in-process console and RCON command sources.
 *
 * @param sender The Bukkit [CommandSender] representing the console or RCON source.
 */
public class PaperConsoleKCommandSender(
    sender: CommandSender
) : ConsoleKCommandSender,
    KPermissible by sender.asKPermissible(),
    KAudience by sender.asKAudience(),
    KCommandDispatcher by sender.asKCommandDispatcher()
