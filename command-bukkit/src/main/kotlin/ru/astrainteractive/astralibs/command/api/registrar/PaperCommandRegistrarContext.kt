package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.event.flowLifecycleEvent

/**
 * [CommandRegistrarContext] that registers Brigadier commands via Paper's [LifecycleEvents.COMMANDS].
 * Replays the latest dispatcher to late subscribers so commands registered after the initial event
 * reach the server immediately.
 *
 * Paper fires the event only while the server starts or reloads, so a plugin enabled on a running
 * server (e.g. by PlugmanX) starts from the dispatcher the server already has.
 */
class PaperCommandRegistrarContext(
    mainScope: CoroutineScope,
    private val plugin: JavaPlugin
) : CommandRegistrarContext {
    private val commandDispatcherFlow = plugin
        .flowLifecycleEvent(LifecycleEvents.COMMANDS)
        .filterNotNull()
        .mapNotNull { registrarEvent -> registrarEvent?.registrar()?.dispatcher }
        .onStart { runningServerDispatcher()?.let { dispatcher -> emit(dispatcher) } }
        // Bukkit's /reload fires the event with the dispatcher the server already had
        .distinctUntilChanged { old, new -> old === new }
        .shareIn(mainScope, SharingStarted.Eagerly, 1)

    /**
     * Paper hands the dispatcher out only inside the event, so outside it the dispatcher is read
     * from Paper's own registrar. Null while the server starts: the event is yet to come then.
     */
    private fun runningServerDispatcher(): CommandDispatcher<CommandSourceStack>? {
        // The initial event fires before the first tick
        if (Bukkit.getCurrentTick() == 0) return null
        return runCatching {
            val paperCommands = Class.forName("io.papermc.paper.command.brigadier.PaperCommands")
            val instance = paperCommands.getField("INSTANCE").get(null)
            paperCommands.getMethod("getDispatcherInternal").invoke(instance) as CommandDispatcher<CommandSourceStack>
        }.onFailure { throwable ->
            plugin.logger.warning("Commands will wait for /minecraft:reload: ${throwable.message}")
        }.getOrNull()
    }

    /** A tree changed outside the dispatch is missing from the trees players already hold. */
    private fun resendCommandTree() {
        Bukkit.getOnlinePlayers().forEach(Player::updateCommands)
    }

    /**
     * Brigadier itself cannot drop a node, but Paper's command map is a live view over the server's
     * Brigadier tree: removing a label there removes the node it stands for.
     */
    private fun unregister(node: LiteralArgumentBuilder<CommandSourceStack>) {
        Bukkit.getServer().commandMap
            .knownCommands
            .remove(node.literal)
            ?: return
        resendCommandTree()
    }

    override fun registerWhenReady(
        node: LiteralArgumentBuilder<*>,
        scope: CoroutineScope
    ) {
        val platformNode = node as LiteralArgumentBuilder<CommandSourceStack>
        commandDispatcherFlow
            .map { dispatcher ->
                dispatcher.register(platformNode)
                resendCommandTree()
            }
            .onCompletion { unregister(platformNode) }
            .launchIn(scope)
    }
}
