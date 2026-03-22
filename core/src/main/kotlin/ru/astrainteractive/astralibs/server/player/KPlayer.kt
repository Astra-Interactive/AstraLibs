package ru.astrainteractive.astralibs.server.player

import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import ru.astrainteractive.astralibs.server.permission.KPermissible
import java.net.InetSocketAddress
import java.util.UUID

@SubclassOptInRequired(InternalPlatformApi::class)
interface KPlayer {
    val uuid: UUID
    val name: String?

    fun hasPlayedBefore(): Boolean
}

@SubclassOptInRequired(InternalPlatformApi::class)
interface OnlineKPlayer :
    KPlayer,
    KAudience,
    Locatable,
    Teleportable,
    KPermissible,
    KCommandDispatcher {
    val address: InetSocketAddress
    override val name: String
}
