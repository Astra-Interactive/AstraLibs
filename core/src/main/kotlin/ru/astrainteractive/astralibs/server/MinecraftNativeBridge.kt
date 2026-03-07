package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.permission.Permissible
import ru.astrainteractive.astralibs.server.player.MinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot
import java.util.UUID

interface MinecraftNativeBridge {
    fun OnlineMinecraftPlayerSnapshot.asAudience(): Audience
    fun OnlineMinecraftPlayerSnapshot.asLocatable(): Locatable
    fun OnlineMinecraftPlayerSnapshot.asTeleportable(): Teleportable
    fun MinecraftPlayerSnapshot.asPermissible(): Permissible
    fun findPlayer(uuid: UUID): MinecraftPlayerSnapshot?
}
