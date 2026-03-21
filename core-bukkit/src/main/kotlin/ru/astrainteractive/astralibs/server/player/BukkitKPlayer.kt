package ru.astrainteractive.astralibs.server.player

import org.bukkit.OfflinePlayer
import java.util.UUID

class BukkitKPlayer(val instance: OfflinePlayer) : KPlayer {
    override val uuid: UUID
        get() = instance.uniqueId

    override val name: String?
        get() = instance.name

    override fun hasPlayedBefore(): Boolean {
        return instance.hasPlayedBefore()
    }
}
