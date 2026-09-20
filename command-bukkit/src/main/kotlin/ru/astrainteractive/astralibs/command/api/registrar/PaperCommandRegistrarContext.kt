package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.shareIn
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.event.flowLifecycleEvent

/**
 * [CommandRegistrarContext] that registers Brigadier commands via Paper's [LifecycleEvents.COMMANDS].
 * Replays the latest dispatcher to late subscribers so commands registered after the initial event
 * reach the server immediately.
 */
class PaperCommandRegistrarContext(
    mainScope: CoroutineScope,
    plugin: JavaPlugin
) : CommandRegistrarContext {
    private val commandDispatcherFlow = plugin
        .flowLifecycleEvent(LifecycleEvents.COMMANDS)
        .filterNotNull()
        .mapNotNull { registrarEvent -> registrarEvent?.registrar()?.dispatcher }
        .shareIn(mainScope, SharingStarted.Eagerly, 1)

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
