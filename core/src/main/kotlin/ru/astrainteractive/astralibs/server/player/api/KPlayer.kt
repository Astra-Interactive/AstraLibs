package ru.astrainteractive.astralibs.server.player.api

import ru.astrainteractive.astralibs.server.api.InternalPlatformApi
import ru.astrainteractive.astralibs.server.api.KAudience
import ru.astrainteractive.astralibs.server.api.KCommandDispatcher
import ru.astrainteractive.astralibs.server.api.Locatable
import ru.astrainteractive.astralibs.server.api.Teleportable
import ru.astrainteractive.astralibs.server.permission.api.KPermissible
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
public interface KPlayer {
    public val uuid: UUID
    public val name: String?

    /**
     * Returns `true` if this player has connected to the server at least once.
     */
    public fun hasPlayedBefore(): Boolean
}

/**
 * Platform-agnostic representation of a player who is currently connected to the server.
 *
 * Combines message-sending in the client language ([KAudience]), location ([Locatable]), teleportation
 * ([Teleportable]), permission checking ([KPermissible]), and command dispatching ([KCommandDispatcher])
 * into a single interface.
 *
 * Subclassing outside the library requires opting in to [InternalPlatformApi].
 *
 * @property address The player's network address.
 * @property name The player's current display name (always non-null for an online player).
 */
@SubclassOptInRequired(InternalPlatformApi::class)
public interface OnlineKPlayer :
    KPlayer,
    KAudience,
    Locatable,
    Teleportable,
    KPermissible,
    KCommandDispatcher {
    public val address: InetSocketAddress
    override val name: String
}
