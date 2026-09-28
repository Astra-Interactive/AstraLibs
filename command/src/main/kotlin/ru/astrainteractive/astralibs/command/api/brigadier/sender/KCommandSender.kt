package ru.astrainteractive.astralibs.command.api.brigadier.sender

import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

/** Sealed abstraction for any entity that can execute a command; replies go out in its [KAudience.locale]. */
sealed interface KCommandSender : KAudience

/** Represents the server console as a command sender. */
interface ConsoleKCommandSender :
    KCommandSender,
    KAudience,
    KPermissible,
    KCommandDispatcher

/** Wraps an [OnlineKPlayer] as a command sender; it reports the player's client language. */
class KPlayerKCommandSender(
    val instance: OnlineKPlayer
) : KCommandSender,
    KAudience by instance,
    KPermissible by instance,
    KCommandDispatcher by instance
