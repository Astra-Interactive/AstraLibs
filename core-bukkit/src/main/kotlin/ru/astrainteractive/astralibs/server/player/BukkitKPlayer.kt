package ru.astrainteractive.astralibs.server.player

import org.bukkit.OfflinePlayer
import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import java.util.UUID

@OptIn(InternalPlatformApi::class)
class BukkitKPlayer(val instance: OfflinePlayer) : KPlayer {
    override val uuid: UUID
        get() = instance.uniqueId

    override val name: String?
        get() = instance.name

    override fun hasPlayedBefore(): Boolean {
        return instance.hasPlayedBefore()
    }
}
