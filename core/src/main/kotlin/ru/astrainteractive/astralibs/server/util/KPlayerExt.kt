package ru.astrainteractive.astralibs.server.util

import ru.astrainteractive.astralibs.server.player.api.KPlayer
import ru.astrainteractive.astralibs.server.player.api.OnlineKPlayer
import ru.astrainteractive.astralibs.server.player.model.OfflineKPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.model.OnlineKPlayerSnapshot

/** Snapshots this player's [uuid] as an [OfflineKPlayerSnapshot]. */
public fun KPlayer.asOfflineSnapshot(): OfflineKPlayerSnapshot {
    return OfflineKPlayerSnapshot(uuid)
}

/** Snapshots this player's current [uuid], [name], and [address] as an [OnlineKPlayerSnapshot]. */
public fun OnlineKPlayer.asOnlineSnapshot(): OnlineKPlayerSnapshot {
    return OnlineKPlayerSnapshot(
        uuid = uuid,
        name = name,
        address = address
    )
}
