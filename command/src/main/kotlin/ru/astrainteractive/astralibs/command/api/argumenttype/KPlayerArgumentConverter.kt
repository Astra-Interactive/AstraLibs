package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException
import ru.astrainteractive.astralibs.server.bridge.PlatformServer
import ru.astrainteractive.astralibs.server.player.KPlayer

/** Converts a player name to a [KPlayer] (online or offline). */
class KPlayerArgumentConverter(
    private val platformServer: PlatformServer
) : ArgumentConverter<KPlayer> {
    override fun transform(argument: String): KPlayer {
        return platformServer.findOfflinePlayer(argument) ?: throw NoPlayerException(argument)
    }
}
