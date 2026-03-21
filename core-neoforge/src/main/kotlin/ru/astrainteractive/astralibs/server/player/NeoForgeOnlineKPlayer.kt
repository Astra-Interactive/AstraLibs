package ru.astrainteractive.astralibs.server.player

import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.KAudience
import ru.astrainteractive.astralibs.server.KCommandDispatcher
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.permission.KPermissible
import ru.astrainteractive.astralibs.server.util.asKAudience
import ru.astrainteractive.astralibs.server.util.asKCommandDispatcher
import ru.astrainteractive.astralibs.server.util.asLocatable
import ru.astrainteractive.astralibs.server.util.asPermissible
import ru.astrainteractive.astralibs.server.util.asTeleportable
import ru.astrainteractive.astralibs.server.util.toPlain
import ru.astrainteractive.klibs.mikro.core.util.cast
import java.net.InetSocketAddress
import java.util.*

class NeoForgeOnlineKPlayer(val instance: ServerPlayer) :
    OnlineKPlayer,
    KAudience by instance.asKAudience(),
    Locatable by instance.asLocatable(),
    Teleportable by instance.asTeleportable(),
    KPermissible by instance.asPermissible(),
    KCommandDispatcher by instance.asKCommandDispatcher() {
    override val uuid: UUID
        get() = instance.uuid
    override val name: String
        get() = instance.name.toPlain()

    override fun hasPlayedBefore(): Boolean {
        return instance.server.playerList
            .load(instance)
            .isPresent
    }

    override val address: InetSocketAddress
        get() = instance.connection
            .remoteAddress
            .cast<InetSocketAddress>()
}
