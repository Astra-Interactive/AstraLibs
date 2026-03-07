package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.player.OfflineMinecraftPlayerSnapshot
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayerSnapshot

fun ServerPlayer.asOnlineMinecraftPlayer(): OnlineMinecraftPlayerSnapshot {
    return OnlineMinecraftPlayerSnapshot(
        uuid = uuid,
        name = name.toPlain(),
        ipAddress = ipAddress
    )
}

fun GameProfile.asOfflineMinecraftPlayer(): OfflineMinecraftPlayerSnapshot {
    return OfflineMinecraftPlayerSnapshot(
        uuid = id
    )
}
