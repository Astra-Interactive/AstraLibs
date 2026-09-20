package ru.astrainteractive.astralibs.command.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import net.minecraft.commands.CommandSourceStack
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.server.ServerLifecycleHooks
import ru.astrainteractive.astralibs.command.api.registrar.CommandRegistrarContext
import ru.astrainteractive.astralibs.event.flowEvent

/**
 * Forge implementation of [CommandRegistrarContext] that registers Brigadier commands via
 * the Forge [RegisterCommandsEvent].
 *
 * The latest event is kept so a node handed over later - a feature enabled while the server runs -
 * reaches the dispatcher right away, and is re-applied every time the event fires again, which
 * covers both initial server start-up and `/reload` scenarios.
 *
 * @param mainScope Scope the event is shared in. Cancelling it stops feeding new events to nodes.
 */
class ForgeCommandRegistrarContext(
    mainScope: CoroutineScope
) : CommandRegistrarContext {
    private val registerCommandsEvent = flowEvent<RegisterCommandsEvent>(EventPriority.HIGHEST)
        .filterNotNull()
        .stateIn(mainScope, SharingStarted.Eagerly, null)

    /** A tree changed outside the event is missing from the trees players already hold. */
    private fun resendCommandTree() {
        val server = ServerLifecycleHooks.getCurrentServer() ?: return
        server.playerList.players.forEach(server.commands::sendCommands)
    }

    private fun unregister(node: LiteralArgumentBuilder<CommandSourceStack>) {
        registerCommandsEvent.value
            ?.dispatcher
            ?.takeIf { commandDispatcher -> commandDispatcher.removeCommand(node.literal) }
            ?.run { resendCommandTree() }
    }

    override fun registerWhenReady(
        node: LiteralArgumentBuilder<*>,
        scope: CoroutineScope
    ) {
        val platformNode = node as LiteralArgumentBuilder<CommandSourceStack>
        registerCommandsEvent
            .mapNotNull { registerCommandsEvent -> registerCommandsEvent?.dispatcher }
            .map { commandDispatcher ->
                commandDispatcher.register(platformNode)
                resendCommandTree()
            }
            .onCompletion { unregister(platformNode) }
            .launchIn(scope)
    }
}
