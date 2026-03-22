package ru.astrainteractive.astralibs.server.player

import com.mojang.authlib.GameProfile
import net.minecraft.world.level.storage.LevelResource
import net.neoforged.neoforge.server.ServerLifecycleHooks
import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import java.util.UUID

@OptIn(InternalPlatformApi::class)
class NeoForgeKPlayer(val instance: GameProfile) : KPlayer {
    override val uuid: UUID
        get() = instance.id

    override val name: String?
        get() = instance.name

    override fun hasPlayedBefore(): Boolean {
        val playerDataDir = ServerLifecycleHooks.getCurrentServer()
            ?.getWorldPath(LevelResource.PLAYER_DATA_DIR)
            ?.toFile()
        return playerDataDir?.resolve("$uuid.dat")?.exists() == true
    }
}
