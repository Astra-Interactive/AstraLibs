package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.event.flowLifecycleEvent

/**
 * [CommandRegistrarContext] that registers Brigadier commands via Paper's [LifecycleEvents.COMMANDS].
 * Replays the latest event to late subscribers so commands registered after the initial event fire immediately.
 */
class PaperCommandRegistrarContext(
    private val mainScope: CoroutineScope,
    plugin: JavaPlugin
) : CommandRegistrarContext {
    private val commandsRegistrarFlow = plugin
        .flowLifecycleEvent(LifecycleEvents.COMMANDS)
        .shareIn(mainScope, SharingStarted.Eagerly, 1)

    override fun registerWhenReady(node: LiteralArgumentBuilder<*>) {
        node as LiteralArgumentBuilder<CommandSourceStack>
        commandsRegistrarFlow
            .filterNotNull()
            .mapNotNull { registrarEvent -> registrarEvent.registrar() }
            .onEach { commands -> commands.register(node.build()) }
            .launchIn(mainScope)
    }
}
