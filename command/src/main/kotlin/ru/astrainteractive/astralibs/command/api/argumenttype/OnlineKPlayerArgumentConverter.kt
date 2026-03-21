package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import ru.astrainteractive.astralibs.server.bridge.PlatformServer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

class OnlineKPlayerArgumentConverter(
    private val platformServer: PlatformServer
) : ArgumentConverter<OnlineKPlayer> {
    override fun transform(argument: String): OnlineKPlayer {
        return platformServer.findOnlinePlayer(argument) ?: throw ArgumentConverterException(
            OnlineKPlayerArgumentConverter::class.java,
            "Player not found $argument"
        )
    }
}
