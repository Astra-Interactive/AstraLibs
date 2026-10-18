package ru.astrainteractive.astralibs.server.bridge.api

import ru.astrainteractive.astralibs.server.player.api.KPlayer
import ru.astrainteractive.astralibs.server.player.api.OnlineKPlayer
import java.util.UUID

/**
 * Platform-agnostic representation of the server, providing access to online and offline players.
 *
 * Implementations wrap the platform-specific server object (e.g. Bukkit's `Server`,
 * NeoForge's `MinecraftServer`) and expose a unified API.
 */
public interface PlatformServer {
    /** Returns all currently connected players. */
    public fun getOnlinePlayers(): List<OnlineKPlayer>

    /** Finds the online player with the given [uuid], or `null` if not online. */
    public fun findOnlinePlayer(uuid: UUID): OnlineKPlayer?

    /** Finds the player with the given [uuid], or `null` if never joined. */
    public fun findOfflinePlayer(uuid: UUID): KPlayer?

    /** Finds the online player with the given [name], or `null` if not online. */
    public fun findOnlinePlayer(name: String): OnlineKPlayer?

    /** Finds the player with the given [name], or `null` if not known to the server. */
    public fun findOfflinePlayer(name: String): KPlayer?
}
