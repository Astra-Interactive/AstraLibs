package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import ru.astrainteractive.astralibs.server.bridge.PlatformServer
import ru.astrainteractive.astralibs.server.player.KPlayer

class KPlayerArgumentConverter(
    private val platformServer: PlatformServer
) : ArgumentConverter<KPlayer> {
    override fun transform(argument: String): KPlayer {
        return platformServer.findOfflinePlayer(argument) ?: throw ArgumentConverterException(
            OnlineKPlayerArgumentConverter::class.java,
            "Player not found $argument"
        )
    }
}
