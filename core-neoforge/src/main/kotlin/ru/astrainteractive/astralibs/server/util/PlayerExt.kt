package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayer
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

fun ServerPlayer.asOnlineMinecraftPlayer(): OnlineMinecraftPlayer {
    return OnlineMinecraftPlayer(
        uuid = uuid,
        name = name.toPlain(),
        ipAddress = ipAddress
    )
}

fun GameProfile.asOfflineMinecraftPlayer(): OfflineMinecraftPlayer {
    return OfflineMinecraftPlayer(
        uuid = id
    )
}
