package ru.astrainteractive.astralibs.server.util

import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OfflineKPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayerSnapshot

fun KPlayer.asOfflineSnapshot(): OfflineKPlayerSnapshot {
    return OfflineKPlayerSnapshot(uuid)
}

fun OnlineKPlayer.asOnlineSnapshot(): OnlineKPlayerSnapshot {
    return OnlineKPlayerSnapshot(
        uuid = uuid,
        name = name,
        address = address
    )
}
