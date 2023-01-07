package ru.astrainteractive.astralibs.utils

import ru.astrainteractive.astralibs.events.DSLEvent
import ru.astrainteractive.astralibs.events.EventManager
import ru.astrainteractive.astralibs.events.GlobalEventManager
import io.netty.channel.Channel
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import net.minecraft.network.PacketListener
import net.minecraft.network.protocol.Packet
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerRespawnEvent
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.HashMap

/**
 * This packet reader class allows you to read minecraft packets without ProtocolLib
 */
abstract class AstraPacketReader<K : PacketListener, T : Packet<K>>(
    /**
     * Example: PacketPlayInUseEntity::class.java
     */
    val clazz: Class<out T>,
    /**
     * You can reassign eventManager to another event
     */
    val eventManager: EventManager = GlobalEventManager
) {
    private val channels: MutableMap<UUID, Channel> = HashMap()
    companion object{
        const val PACKET_ID = "PacketInjector"
    }


    /**
     * In plugin initialization you should enable it
     */
    fun onEnable() {
        onDisable()
        Bukkit.getOnlinePlayers().forEach { inject(it) }
        val joinEvent = DSLEvent.event<PlayerJoinEvent>(eventManager) {
            inject(it.player)
        }
        val respawnEvent = DSLEvent.event<PlayerRespawnEvent>(eventManager) {
            inject(it.player)
        }
        val quitEvent = DSLEvent.event<PlayerQuitEvent>(eventManager) {
            deInject(it.player.uniqueId)
        }
        val deathEvent = DSLEvent.event<PlayerDeathEvent>(eventManager) {
            deInject(it.player.uniqueId)
        }
    }

    /**
     * Need to disable so all channels will be cleared
     */
    fun onDisable() {
        channels.keys.toList().forEach { deInject(it) }
        channels.clear()
        eventManager.onDisable()
    }

    /**
     * Here you should provide a channel for your current version
     * Example, for v1_19_R1 it's (this as CraftPlayer).handle.b.b.m
     */
    abstract val Player.provideChannel: Channel



    /**
     * In your plugin you probably will need only this function
     * It will return played packet
     */
    abstract fun readPacket(player: Player, packet: T)

    fun decoder(player: Player) = object : MessageToMessageDecoder<T>(clazz) {
        override fun decode(ctx: ChannelHandlerContext?, packet: T?, arg: MutableList<Any>?) {
            packet ?: return
            arg ?: return
            ctx ?: return
            arg.add(packet as Any)
            readPacket(player, packet)
        }
    }

    @Synchronized
    fun inject(player: Player) {
        val channel = player.provideChannel
        channels[player.uniqueId] = channel
        if (channel.pipeline().get(PACKET_ID) != null) return
        channel.pipeline().addAfter("decoder", PACKET_ID, decoder(player))
    }

    @Synchronized
    fun deInject(uuid: UUID) {
        val channel = channels[uuid] ?: return
        kotlin.runCatching { channel.pipeline().remove(PACKET_ID) }
        channels.remove(uuid)
    }
}