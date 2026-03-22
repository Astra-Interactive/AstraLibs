package ru.astrainteractive.astralibs.server.player

import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.permission.asKPermissible
import ru.astrainteractive.astralibs.server.util.asKAudience
import ru.astrainteractive.astralibs.server.util.asKCommandDispatcher
import ru.astrainteractive.astralibs.server.util.asLocatable
import ru.astrainteractive.astralibs.server.util.asTeleportable
import java.net.InetSocketAddress
import java.util.UUID

@OptIn(InternalPlatformApi::class)
class BukkitOnlineKPlayer(val instance: Player) :
    OnlineKPlayer,
    KAudience by instance.asKAudience(),
    Locatable by instance.asLocatable(),
    Teleportable by instance.asTeleportable(),
    KPermissible by instance.asKPermissible(),
    KCommandDispatcher by instance.asKCommandDispatcher() {
    override val uuid: UUID
        get() = instance.uniqueId
    override val address: InetSocketAddress
        get() = instance.address
    override val name: String
        get() = instance.name

    override fun hasPlayedBefore(): Boolean {
        return instance.hasPlayedBefore()
    }
}
