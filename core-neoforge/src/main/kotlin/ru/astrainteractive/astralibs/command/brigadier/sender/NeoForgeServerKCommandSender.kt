package ru.astrainteractive.astralibs.command.brigadier.sender

import net.minecraft.server.MinecraftServer
import ru.astrainteractive.astralibs.command.api.brigadier.sender.ConsoleKCommandSender
import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.MinecraftServerKAudience
import ru.astrainteractive.astralibs.server.permission.ConsoleKPermissible
import ru.astrainteractive.astralibs.server.permission.KPermissible

class NeoForgeServerKCommandSender(
    sender: MinecraftServer
) : ConsoleKCommandSender,
    KPermissible by ConsoleKPermissible,
    KAudience by MinecraftServerKAudience(sender)
