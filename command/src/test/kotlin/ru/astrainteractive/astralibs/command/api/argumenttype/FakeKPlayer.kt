package ru.astrainteractive.astralibs.command.api.argumenttype

import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import ru.astrainteractive.astralibs.server.player.KPlayer
import java.util.UUID

/** A player who has joined the server before and is offline now. */
@OptIn(InternalPlatformApi::class)
internal class FakeKPlayer(
    override val name: String,
    override val uuid: UUID = UUID.nameUUIDFromBytes(name.toByteArray())
) : KPlayer {
    override fun hasPlayedBefore(): Boolean = true
}
