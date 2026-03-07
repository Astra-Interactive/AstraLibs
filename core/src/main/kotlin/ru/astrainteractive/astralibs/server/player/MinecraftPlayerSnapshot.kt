package ru.astrainteractive.astralibs.server.player

import kotlinx.serialization.Serializable
import ru.astrainteractive.klibs.mikro.extensions.serialization.UUIDSerializer
import java.util.UUID

interface MinecraftPlayerSnapshot {
    val uuid: UUID
}

@Serializable
class OnlineMinecraftPlayerSnapshot(
    @Serializable(UUIDSerializer::class)
    override val uuid: UUID,
    val name: String,
    val ipAddress: String
) : MinecraftPlayerSnapshot

@Serializable
class OfflineMinecraftPlayerSnapshot(
    @Serializable(UUIDSerializer::class)
    override val uuid: UUID
) : MinecraftPlayerSnapshot
