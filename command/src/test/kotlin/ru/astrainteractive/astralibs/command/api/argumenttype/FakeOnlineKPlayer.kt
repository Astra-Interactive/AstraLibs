package ru.astrainteractive.astralibs.command.api.argumenttype

import net.kyori.adventure.text.Component
import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import ru.astrainteractive.astralibs.server.location.KLocation
import ru.astrainteractive.astralibs.server.permission.Permission
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer
import java.net.InetSocketAddress
import java.util.Locale
import java.util.UUID

/** An online player that only has an identity: converters return it and must not use anything else. */
@OptIn(InternalPlatformApi::class)
internal class FakeOnlineKPlayer(
    override val name: String,
    override val uuid: UUID = UUID.nameUUIDFromBytes(name.toByteArray())
) : OnlineKPlayer {
    override val address: InetSocketAddress = InetSocketAddress.createUnresolved("localhost", 25565)

    override val locale: Locale = Locale.ROOT

    override fun hasPlayedBefore(): Boolean = true

    override fun sendMessage(component: Component) = error("A converter must not message $name")

    override fun getLocation(): KLocation = error("A converter must not locate $name")

    override fun teleport(kLocation: KLocation) = error("A converter must not teleport $name")

    override fun hasPermission(permission: Permission): Boolean = error("A converter must not check $name")

    override fun maxPermissionSize(permission: Permission): Int? = error("A converter must not check $name")

    override fun minPermissionSize(permission: Permission): Int? = error("A converter must not check $name")

    override fun permissionSizes(permission: Permission): List<Int> = error("A converter must not check $name")

    override fun dispatchCommand(command: String) = error("A converter must not run commands as $name")
}
