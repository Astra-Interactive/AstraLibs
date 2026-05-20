package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.MinecraftKPlayer
import ru.astrainteractive.astralibs.server.player.MinecraftOnlineKPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

fun ServerPlayer.asOnlineMinecraftPlayer(): OnlineKPlayer {
    return MinecraftOnlineKPlayer(this)
}

fun GameProfile.asOfflineMinecraftPlayer(): KPlayer {
    return MinecraftKPlayer(this)
}
