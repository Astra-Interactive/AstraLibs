package ru.astrainteractive.astralibs.command.argumenttype

import ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter
import ru.astrainteractive.astralibs.command.model.ArgumentConverterException
import ru.astrainteractive.astralibs.server.bridge.api.PlatformServer
import ru.astrainteractive.astralibs.server.player.api.KPlayer

/** Converts a player name to a [KPlayer] (online or offline). */
public class KPlayerArgumentConverter(
    private val platformServer: PlatformServer
) : ArgumentConverter<KPlayer> {
    override fun transform(argument: String): KPlayer {
        return platformServer.findOfflinePlayer(argument) ?: throw ArgumentConverterException(
            OnlineKPlayerArgumentConverter::class.java,
            "Player not found $argument"
        )
    }
}
