package ru.astrainteractive.astralibs.server.player

import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import ru.astrainteractive.astralibs.server.permission.KPermissible
import java.net.InetSocketAddress
import java.util.UUID

/**
 * Platform-agnostic representation of a Minecraft player, which may be online or offline.
 *
 * Subclassing outside the library requires opting in to [InternalPlatformApi].
 *
 * @property uuid The player's unique identifier.
 * @property name The player's last known name, or `null` if unavailable for an offline player.
 */
@SubclassOptInRequired(InternalPlatformApi::class)
interface KPlayer {
    val uuid: UUID
    val name: String?

    /**
     * Returns `true` if this player has connected to the server at least once.
     */
    fun hasPlayedBefore(): Boolean
}

/**
 * Platform-agnostic representation of a player who is currently connected to the server.
 *
 * Combines message-sending ([KAudience]), location ([Locatable]), teleportation ([Teleportable]),
 * permission checking ([KPermissible]), and command dispatching ([KCommandDispatcher]) into a
 * single interface.
 *
 * Subclassing outside the library requires opting in to [InternalPlatformApi].
 *
 * @property address The player's network address.
 * @property name The player's current display name (always non-null for an online player).
 */
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
