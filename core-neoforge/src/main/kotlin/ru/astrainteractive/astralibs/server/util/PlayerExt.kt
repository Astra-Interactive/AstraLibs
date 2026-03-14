package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.player.KPlayer
import ru.astrainteractive.astralibs.server.player.NeoForgeKPlayer
import ru.astrainteractive.astralibs.server.player.NeoForgeOnlineKPlayer
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

fun ServerPlayer.asOnlineMinecraftPlayer(): OnlineKPlayer {
    return NeoForgeOnlineKPlayer(this)
}

fun GameProfile.asOfflineMinecraftPlayer(): KPlayer {
    return NeoForgeKPlayer(this)
}
