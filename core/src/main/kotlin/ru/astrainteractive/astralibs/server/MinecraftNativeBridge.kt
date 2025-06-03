package ru.astrainteractive.astralibs.server

import ru.astrainteractive.astralibs.permission.Permissible
import ru.astrainteractive.astralibs.server.player.MinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer
import java.util.UUID

interface MinecraftNativeBridge {
    fun OnlineMinecraftPlayer.asAudience(): Audience
    fun OnlineMinecraftPlayer.asLocatable(): Locatable
    fun OnlineMinecraftPlayer.asTeleportable(): Teleportable
    fun MinecraftPlayer.asPermissible(): Permissible
    fun findPlayer(uuid: UUID): MinecraftPlayer?
}
