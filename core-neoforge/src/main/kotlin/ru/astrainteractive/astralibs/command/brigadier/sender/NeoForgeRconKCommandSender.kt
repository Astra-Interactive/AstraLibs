package ru.astrainteractive.astralibs.command.brigadier.sender

import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.command.api.brigadier.sender.ConsoleKCommandSender
import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.permission.ConsoleKPermissible
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.util.asKAudience
import ru.astrainteractive.astralibs.server.util.asKCommandDispatcher

class NeoForgeRconKCommandSender(
    sender: RconConsoleSource
) : ConsoleKCommandSender,
    KPermissible by ConsoleKPermissible,
    KAudience by sender.asKAudience(),
    KCommandDispatcher by sender.asKCommandDispatcher()
