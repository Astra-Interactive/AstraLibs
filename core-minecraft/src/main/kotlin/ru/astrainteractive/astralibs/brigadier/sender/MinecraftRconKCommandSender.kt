package ru.astrainteractive.astralibs.brigadier.sender

import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.command.sender.model.ConsoleKCommandSender
import ru.astrainteractive.astralibs.server.api.KAudience
import ru.astrainteractive.astralibs.server.api.KCommandDispatcher
import ru.astrainteractive.astralibs.server.permission.api.KPermissible
import ru.astrainteractive.astralibs.server.permission.stub.ConsoleKPermissible
import ru.astrainteractive.astralibs.server.util.asKAudience
import ru.astrainteractive.astralibs.server.util.asKCommandDispatcher

/** [ConsoleKCommandSender] backed by a Minecraft RCON connection. Always grants all permissions. */
public class MinecraftRconKCommandSender(
    sender: RconConsoleSource
) : ConsoleKCommandSender,
    KPermissible by ConsoleKPermissible,
    KAudience by sender.asKAudience(),
    KCommandDispatcher by sender.asKCommandDispatcher()
