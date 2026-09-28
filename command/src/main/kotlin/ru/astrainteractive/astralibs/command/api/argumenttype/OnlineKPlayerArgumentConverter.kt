package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.NoPlayerException
import ru.astrainteractive.astralibs.server.bridge.PlatformServer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

/**
 * Converts a player name to a currently online [OnlineKPlayer].
 *
 * @throws NoPlayerException when no online player has this name.
 */
class OnlineKPlayerArgumentConverter(
    private val platformServer: PlatformServer
) : ArgumentConverter<OnlineKPlayer> {
    override fun transform(argument: String): OnlineKPlayer {
        return platformServer.findOnlinePlayer(argument) ?: throw NoPlayerException(argument)
    }
}
