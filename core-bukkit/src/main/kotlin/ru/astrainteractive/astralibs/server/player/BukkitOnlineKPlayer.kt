package ru.astrainteractive.astralibs.server.player

import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Audience
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.permission.asKPermissible
import ru.astrainteractive.astralibs.server.util.asAudience
import ru.astrainteractive.astralibs.server.util.asLocatable
import ru.astrainteractive.astralibs.server.util.asTeleportable
import java.net.InetSocketAddress
import java.util.UUID

class BukkitOnlineKPlayer(val instance: Player) :
    OnlineKPlayer,
    Audience by instance.asAudience(),
    Locatable by instance.asLocatable(),
    Teleportable by instance.asTeleportable(),
    KPermissible by instance.asKPermissible() {
    override val uuid: UUID
        get() = instance.uniqueId
    override val address: InetSocketAddress
        get() = instance.address
    override val name: String
        get() = instance.name
}
