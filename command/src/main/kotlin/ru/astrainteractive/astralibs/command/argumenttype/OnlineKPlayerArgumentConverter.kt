package ru.astrainteractive.astralibs.command.argumenttype

import ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter
import ru.astrainteractive.astralibs.command.model.ArgumentConverterException
import ru.astrainteractive.astralibs.server.bridge.api.PlatformServer
import ru.astrainteractive.astralibs.server.player.api.OnlineKPlayer

/** Converts a player name to a currently online [OnlineKPlayer]. */
public class OnlineKPlayerArgumentConverter(
    private val platformServer: PlatformServer
) : ArgumentConverter<OnlineKPlayer> {
    override fun transform(argument: String): OnlineKPlayer {
        return platformServer.findOnlinePlayer(argument) ?: throw ArgumentConverterException(
            OnlineKPlayerArgumentConverter::class.java,
            "Player not found $argument"
        )
    }
}
