package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import ru.astrainteractive.astralibs.server.api.MinecraftServerRegistry
import java.util.UUID

/** Returns the Overworld dimension path (e.g. `"overworld"`), or `null` when no server is available. */
public fun MinecraftServerRegistry.getDefaultWorldName(): String? {
    return serverOrNull
        ?.getLevel(Level.OVERWORLD)
        ?.dimension()
        ?.location()
        ?.path
}

/** Returns the cached [GameProfile] for [uuid], or `null` if not cached or no server is available. */
public fun MinecraftServerRegistry.getPlayerGameProfile(uuid: UUID): GameProfile? {
    return serverOrNull?.profileCache?.get(uuid)?.orElse(null)
}

/** Returns the cached [GameProfile] for [name], or `null` if not cached or no server is available. */
public fun MinecraftServerRegistry.getPlayerGameProfile(name: String): GameProfile? {
    return serverOrNull?.profileCache?.get(name)?.orElse(null)
}

/** Returns a snapshot of currently online players, or an empty list when no server is available. */
public fun MinecraftServerRegistry.getOnlinePlayers(): List<ServerPlayer> {
    return serverOrNull
        ?.playerList
        ?.players
        ?.toList()
        .orEmpty()
        .filterNotNull()
}

/** Returns the online [ServerPlayer] with [uuid], or `null` if not online or no server is available. */
public fun MinecraftServerRegistry.getOnlinePlayer(uuid: UUID): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayer(uuid)
}

/** Returns the online [ServerPlayer] with [name], or `null` if not online or no server is available. */
public fun MinecraftServerRegistry.getOnlinePlayer(name: String): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayerByName(name)
}

/** Returns the timestamp (ms) of the next scheduled server tick, or `0.0` when no server is available. */
public fun MinecraftServerRegistry.getNextTickTime(): Double {
    return serverOrNull?.nextTickTime?.toDouble() ?: 0.0
}
