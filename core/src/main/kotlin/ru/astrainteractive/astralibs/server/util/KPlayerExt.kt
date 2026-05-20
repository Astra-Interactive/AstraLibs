package ru.astrainteractive.astralibs.server.util

import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OfflineKPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayerSnapshot

/** Snapshots this player's [uuid] as an [OfflineKPlayerSnapshot]. */
fun KPlayer.asOfflineSnapshot(): OfflineKPlayerSnapshot {
    return OfflineKPlayerSnapshot(uuid)
}

/** Snapshots this player's current [uuid], [name], and [address] as an [OnlineKPlayerSnapshot]. */
fun OnlineKPlayer.asOnlineSnapshot(): OnlineKPlayerSnapshot {
    return OnlineKPlayerSnapshot(
        uuid = uuid,
        name = name,
        address = address
    )
}
