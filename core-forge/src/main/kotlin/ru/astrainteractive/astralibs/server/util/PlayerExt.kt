package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.player.ForgeKPlayer
import ru.astrainteractive.astralibs.server.player.ForgeOnlineKPlayer
import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

fun ServerPlayer.asOnlineMinecraftPlayer(): OnlineKPlayer {
    return ForgeOnlineKPlayer(this)
}

fun GameProfile.asOfflineMinecraftPlayer(): KPlayer {
    return ForgeKPlayer(this)
}
