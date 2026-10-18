package ru.astrainteractive.astralibs.server.player

import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.api.InternalPlatformApi
import ru.astrainteractive.astralibs.server.api.KAudience
import ru.astrainteractive.astralibs.server.api.KCommandDispatcher
import ru.astrainteractive.astralibs.server.api.Locatable
import ru.astrainteractive.astralibs.server.api.Teleportable
import ru.astrainteractive.astralibs.server.permission.api.KPermissible
import ru.astrainteractive.astralibs.server.player.api.OnlineKPlayer
import ru.astrainteractive.astralibs.server.util.MinecraftUtil
import ru.astrainteractive.astralibs.server.util.asKAudience
import ru.astrainteractive.astralibs.server.util.asKCommandDispatcher
import ru.astrainteractive.astralibs.server.util.asLocatable
import ru.astrainteractive.astralibs.server.util.asPermissible
import ru.astrainteractive.astralibs.server.util.asTeleportable
import ru.astrainteractive.astralibs.server.util.toPlain
import ru.astrainteractive.klibs.mikro.core.util.tryCast
import java.net.InetSocketAddress
import java.util.UUID

/** [OnlineKPlayer] backed by a vanilla [ServerPlayer]. */
@OptIn(InternalPlatformApi::class)
public class MinecraftOnlineKPlayer(public val instance: ServerPlayer) :
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

    override val address: InetSocketAddress
        get() = instance.connection
            .remoteAddress
            .tryCast<InetSocketAddress>()
            ?: error("Failed to get address from player ${instance.name}")

    override fun hasPlayedBefore(): Boolean {
        return MinecraftUtil.serverOrNull
            ?.playerList
            ?.load(instance)
            ?.isEmpty == true
    }
}
