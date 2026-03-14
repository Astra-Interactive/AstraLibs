package ru.astrainteractive.astralibs.server.player

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.astrainteractive.astralibs.serialization.InetSocketAddressSerializer
import ru.astrainteractive.klibs.mikro.extensions.serialization.UUIDSerializer
import java.net.InetSocketAddress
import java.util.UUID

interface KPlayerSnapshot {
    val uuid: UUID
}

@Serializable
class OnlineKPlayerSnapshot(
    @Serializable(UUIDSerializer::class)
    @SerialName("uuid")
    override val uuid: UUID,
    @SerialName("name")
    val name: String,
    @Serializable(InetSocketAddressSerializer::class)
    @SerialName("address")
    val address: InetSocketAddress
) : KPlayerSnapshot

@Serializable
class OfflineKPlayerSnapshot(
    @Serializable(UUIDSerializer::class)
    @SerialName("uuid")
    override val uuid: UUID
) : KPlayerSnapshot
