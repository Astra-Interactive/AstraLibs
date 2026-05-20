package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import java.lang.ref.WeakReference
import java.util.UUID
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Thread-safe weak-reference registry for the running [MinecraftServer].
 *
 * Call [setServer] with the live instance on startup and `null` on shutdown.
 */
object MinecraftUtil {
    private val lock = ReentrantLock()
    private var _server: WeakReference<MinecraftServer>? = null

    /** Returns the registered server, or `null` if unset or GC'd. */
    val serverOrNull: MinecraftServer?
        get() = lock.withLock { _server?.get() }

    /** Registers or clears the current [MinecraftServer] reference. */
    fun setServer(server: MinecraftServer?) {
        lock.withLock { _server = WeakReference(server) }
    }

    /** Returns the registered server, or throws [IllegalStateException] if unavailable. */
    fun requireServer(): MinecraftServer {
        return lock
            .withLock { _server?.get() }
            ?: error("Server is not available")
    }
}

/** Returns the Overworld dimension path (e.g. `"overworld"`), or `null` when no server is available. */
fun MinecraftUtil.getDefaultWorldName(): String? {
    return serverOrNull
        ?.getLevel(Level.OVERWORLD)
        ?.dimension()
        ?.location()
        ?.path
}

/** Returns the cached [GameProfile] for [uuid], or `null` if not cached or no server is available. */
fun MinecraftUtil.getPlayerGameProfile(uuid: UUID): GameProfile? {
    return serverOrNull?.profileCache?.get(uuid)?.orElse(null)
}

/** Returns the cached [GameProfile] for [name], or `null` if not cached or no server is available. */
fun MinecraftUtil.getPlayerGameProfile(name: String): GameProfile? {
    return serverOrNull?.profileCache?.get(name)?.orElse(null)
}

/** Returns a snapshot of currently online players, or an empty list when no server is available. */
fun MinecraftUtil.getOnlinePlayers(): List<ServerPlayer> {
    return serverOrNull
        ?.playerList
        ?.players
        ?.toList()
        .orEmpty()
        .filterNotNull()
}

/** Returns the online [ServerPlayer] with [uuid], or `null` if not online or no server is available. */
fun MinecraftUtil.getOnlinePlayer(uuid: UUID): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayer(uuid)
}

/** Returns the online [ServerPlayer] with [name], or `null` if not online or no server is available. */
fun MinecraftUtil.getOnlinePlayer(name: String): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayerByName(name)
}

/** Returns the timestamp (ms) of the next scheduled server tick, or `0.0` when no server is available. */
fun MinecraftUtil.getNextTickTime(): Double {
    return serverOrNull?.nextTickTime?.toDouble() ?: 0.0
}
