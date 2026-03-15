package ru.astrainteractive.astralibs.command.api.brigadier.sender

import org.bukkit.command.CommandSender
import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.permission.asKPermissible
import ru.astrainteractive.astralibs.server.util.asKAudience

class PaperConsoleKCommandSender(
    sender: CommandSender
) : ConsoleKCommandSender,
    KPermissible by sender.asKPermissible(),
    KAudience by sender.asKAudience()
