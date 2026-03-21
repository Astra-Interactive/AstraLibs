package ru.astrainteractive.astralibs.command.api.brigadier.sender

import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

sealed interface KCommandSender
interface ConsoleKCommandSender :
    KCommandSender,
    KAudience,
    KPermissible,
    KCommandDispatcher
class KPlayerKCommandSender(
    instance: OnlineKPlayer
) : KCommandSender,
    KCommandDispatcher,
    OnlineKPlayer by instance
