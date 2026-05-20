package ru.astrainteractive.astralibs.server.player

import com.mojang.authlib.GameProfile
import net.minecraft.world.level.storage.LevelResource
import ru.astrainteractive.astralibs.server.annotation.InternalPlatformApi
import ru.astrainteractive.astralibs.server.util.MinecraftUtil
import java.util.UUID

/**
 * [KPlayer] for offline players backed by a [GameProfile].
 *
 * [hasPlayedBefore] checks for a `.dat` file in the world's `playerdata` directory.
 */
@OptIn(InternalPlatformApi::class)
class MinecraftKPlayer(val instance: GameProfile) : KPlayer {
    override val uuid: UUID
        get() = instance.id

    override val name: String?
        get() = instance.name

    override fun hasPlayedBefore(): Boolean {
        val playerDataDir = MinecraftUtil.serverOrNull
            ?.getWorldPath(LevelResource.PLAYER_DATA_DIR)
            ?.toFile()
        return playerDataDir?.resolve("$uuid.dat")?.exists() == true
    }
}
