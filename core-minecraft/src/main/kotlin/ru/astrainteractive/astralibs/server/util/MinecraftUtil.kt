package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import java.lang.ref.WeakReference
import java.util.UUID
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object MinecraftUtil {
    private val lock = ReentrantLock()
    private var _server: WeakReference<MinecraftServer>? = null

    val serverOrNull: MinecraftServer?
        get() = lock.withLock { _server?.get() }

    fun setServer(server: MinecraftServer?) {
        lock.withLock { _server = WeakReference(server) }
    }

    fun requireServer(): MinecraftServer {
        return lock
            .withLock { _server?.get() }
            ?: error("Server is not available")
    }
}

fun MinecraftUtil.getDefaultWorldName(): String? {
    return serverOrNull
        ?.getLevel(Level.OVERWORLD)
        ?.dimension()
        ?.location()
        ?.path
}

fun MinecraftUtil.getPlayerGameProfile(uuid: UUID): GameProfile? {
    return serverOrNull?.profileCache?.get(uuid)?.orElse(null)
}

fun MinecraftUtil.getPlayerGameProfile(name: String): GameProfile? {
    return serverOrNull?.profileCache?.get(name)?.orElse(null)
}

fun MinecraftUtil.getOnlinePlayers(): List<ServerPlayer> {
    return serverOrNull
        ?.playerList
        ?.players
        ?.toList()
        .orEmpty()
        .filterNotNull()
}

fun MinecraftUtil.getOnlinePlayer(uuid: UUID): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayer(uuid)
}

fun MinecraftUtil.getOnlinePlayer(name: String): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayerByName(name)
}

fun MinecraftUtil.getNextTickTime(): Double {
    return serverOrNull?.nextTickTime?.toDouble() ?: 0.0
}
