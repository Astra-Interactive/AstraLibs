package ru.astrainteractive.astralibs.command.brigadier.sender

import net.minecraft.server.MinecraftServer
import ru.astrainteractive.astralibs.command.api.brigadier.sender.ConsoleKCommandSender
import ru.astrainteractive.astralibs.server.api.KAudience
import ru.astrainteractive.astralibs.server.api.KCommandDispatcher
import ru.astrainteractive.astralibs.server.permission.api.KPermissible
import ru.astrainteractive.astralibs.server.permission.stub.ConsoleKPermissible
import ru.astrainteractive.astralibs.server.util.asKAudience
import ru.astrainteractive.astralibs.server.util.asKCommandDispatcher

/**
 * [ConsoleKCommandSender] backed by a [MinecraftServer] (dedicated or integrated).
 *
 * Always grants all permissions, matching vanilla server-console behaviour.
 *
 * @param sender The running [MinecraftServer].
 */
public class MinecraftServerKCommandSender(
    sender: MinecraftServer
) : ConsoleKCommandSender,
    KPermissible by ConsoleKPermissible,
    KAudience by sender.asKAudience(),
    KCommandDispatcher by sender.asKCommandDispatcher()
