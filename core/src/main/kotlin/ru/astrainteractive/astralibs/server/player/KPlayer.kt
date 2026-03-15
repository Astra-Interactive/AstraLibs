package ru.astrainteractive.astralibs.server.player

import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.permission.KPermissible
import java.net.InetSocketAddress
import java.util.UUID

interface KPlayer {
    val uuid: UUID
}

interface OnlineKPlayer : KPlayer, KAudience, Locatable, Teleportable, KPermissible {
    val address: InetSocketAddress
    val name: String
}
