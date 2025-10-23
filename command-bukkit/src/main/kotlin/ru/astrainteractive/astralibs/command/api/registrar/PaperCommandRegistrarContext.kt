package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.astralibs.event.flowLifecycleEvent

class PaperCommandRegistrarContext(
    private val mainScope: CoroutineScope,
    plugin: JavaPlugin
) : CommandRegistrarContext<CommandSourceStack> {
    private val commandsRegistrarFlow = plugin
        .flowLifecycleEvent(LifecycleEvents.COMMANDS)
        .shareIn(mainScope, SharingStarted.Eagerly, 1)

    override fun registerWhenReady(node: LiteralCommandNode<CommandSourceStack>) {
        commandsRegistrarFlow
            .mapNotNull { it?.registrar() }
            .onEach { it.register(node) }
            .launchIn(mainScope)
    }
}
