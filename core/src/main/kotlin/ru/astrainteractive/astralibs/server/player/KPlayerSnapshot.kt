package ru.astrainteractive.astralibs.server.player

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.astrainteractive.astralibs.serialization.InetSocketAddressSerializer
import ru.astrainteractive.klibs.mikro.extensions.serialization.JUuidSerializer
import java.net.InetSocketAddress
import java.util.UUID

/**
 * A serializable snapshot of minimal player identity data.
 *
 * Snapshots are taken from a live [KPlayer] or [OnlineKPlayer] at a point in time and can be
 * persisted or transported without keeping a reference to the live player object.
 *
 * @property uuid The player's unique identifier.
 */
interface KPlayerSnapshot {
    val uuid: UUID
}

/**
 * A serializable snapshot of an online player's identity and connection data.
 *
 * @property uuid The player's unique identifier.
 * @property name The player's display name at the time the snapshot was taken.
 * @property address The player's network address at the time the snapshot was taken.
 */
@Serializable
class OnlineKPlayerSnapshot(
    @Serializable(JUuidSerializer::class)
    @SerialName("uuid")
    override val uuid: UUID,
    @SerialName("name")
    val name: String,
    @Serializable(InetSocketAddressSerializer::class)
    @SerialName("address")
    val address: InetSocketAddress
) : KPlayerSnapshot

/**
 * A serializable snapshot of an offline player containing only their [UUID].
 *
 * @property uuid The player's unique identifier.
 */
@Serializable
class OfflineKPlayerSnapshot(
    @Serializable(JUuidSerializer::class)
    @SerialName("uuid")
    override val uuid: UUID
) : KPlayerSnapshot
