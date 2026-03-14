package ru.astrainteractive.astralibs.server.player

import com.mojang.authlib.GameProfile
import java.util.UUID

class NeoForgeKPlayer(val instance: GameProfile) : KPlayer {
    override val uuid: UUID
        get() = instance.id
}
