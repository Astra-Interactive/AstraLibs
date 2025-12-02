package ru.astrainteractive.astralibs.server.util

import com.mojang.authlib.GameProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.Level
import net.neoforged.fml.loading.FMLLoader
import net.neoforged.neoforge.event.server.ServerStartedEvent
import net.neoforged.neoforge.server.ServerLifecycleHooks
import ru.astrainteractive.astralibs.event.flowEvent
import ru.astrainteractive.klibs.mikro.core.logging.JUtiltLogger
import ru.astrainteractive.klibs.mikro.core.logging.Logger
import java.util.UUID

/**
 * Don't forget to instantiate [NeoForgeUtil] on init at loader
 */
object NeoForgeUtil : Logger by JUtiltLogger("AstraLibs-ForgeUtil") {
    private val serverFlow = flowEvent<ServerStartedEvent>()
        .map { event -> event.server }
        .flowOn(Dispatchers.Unconfined)
        .stateIn(GlobalScope, SharingStarted.Eagerly, null)

    val serverOrNull: MinecraftServer?
        get() {
            val server = serverFlow.value
            if (server == null) {
                error { "#serverOrNull could not get server!" }
            }
            return server
        }

    suspend fun awaitServer(): MinecraftServer {
        return serverFlow.filterNotNull().first()
    }

    fun requireServer(): MinecraftServer {
        return ServerLifecycleHooks
            .getCurrentServer()
            ?: error("Server is not available")
    }

    fun bootstrap() = Unit
}

fun NeoForgeUtil.isModLoaded(modId: String): Boolean {
    return FMLLoader.getLoadingModList().getModFileById(modId) != null
}

fun NeoForgeUtil.getDefaultWorldName(): String? {
    return serverOrNull
        ?.getLevel(Level.OVERWORLD)
        ?.level
        ?.dimension()
        ?.location()
        ?.path
}

fun NeoForgeUtil.getPlayerGameProfile(uuid: UUID): GameProfile? {
    return serverOrNull?.profileCache?.get(uuid)?.orElse(null)
}

fun NeoForgeUtil.getPlayerGameProfile(name: String): GameProfile? {
    return serverOrNull?.profileCache?.get(name)?.orElse(null)
}

fun NeoForgeUtil.getOnlinePlayers(): List<ServerPlayer> {
    return serverOrNull
        ?.playerList
        ?.players
        ?.toList()
        .orEmpty()
        .filterNotNull()
}

fun NeoForgeUtil.getOnlinePlayer(uuid: UUID): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayer(uuid)
}

fun NeoForgeUtil.getOnlinePlayer(name: String): ServerPlayer? {
    return serverOrNull?.playerList?.getPlayerByName(name)
}

fun NeoForgeUtil.getNextTickTime(): Double {
    return serverOrNull?.nextTickTime?.toDouble() ?: 0.0
}
