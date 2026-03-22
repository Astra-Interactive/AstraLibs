package ru.astrainteractive.astralibs.command.api.brigadier.sender

import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

sealed interface KCommandSender : KAudience

interface ConsoleKCommandSender :
    KCommandSender,
    KAudience,
    KPermissible,
    KCommandDispatcher

class KPlayerKCommandSender(
    val instance: OnlineKPlayer
) : KCommandSender,
    KAudience by instance,
    KPermissible by instance,
    KCommandDispatcher by instance
