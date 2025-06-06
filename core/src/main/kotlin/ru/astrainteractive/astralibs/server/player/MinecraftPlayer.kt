package ru.astrainteractive.astralibs.server.player

import kotlinx.serialization.Serializable
import ru.astrainteractive.astralibs.serialization.UuidSerializer
import java.util.UUID

interface MinecraftPlayer {
    val uuid: UUID
}

@Serializable
class OnlineMinecraftPlayer(
    @Serializable(UuidSerializer::class)
    override val uuid: UUID,
    val name: String,
    val ipAddress: String
) : MinecraftPlayer

@Serializable
class OfflineMinecraftPlayer(
    @Serializable(UuidSerializer::class)
    override val uuid: UUID
) : MinecraftPlayer
