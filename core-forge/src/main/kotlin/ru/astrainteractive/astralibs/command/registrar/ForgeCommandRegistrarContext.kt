package ru.astrainteractive.astralibs.command.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import net.minecraft.commands.CommandSourceStack
import net.minecraftforge.event.RegisterCommandsEvent
import net.minecraftforge.eventbus.api.EventPriority
import ru.astrainteractive.astralibs.command.api.registrar.CommandRegistrarContext
import ru.astrainteractive.astralibs.event.flowEvent

class ForgeCommandRegistrarContext(
    private val mainScope: CoroutineScope
) : CommandRegistrarContext {
    private val registerCommandsEvent = flowEvent<RegisterCommandsEvent>(EventPriority.HIGHEST)
        .filterNotNull()
        .stateIn(mainScope, SharingStarted.Eagerly, null)

    override fun registerWhenReady(node: LiteralArgumentBuilder<*>) {
        node as LiteralArgumentBuilder<CommandSourceStack>
        registerCommandsEvent
            .mapNotNull { registerCommandsEvent -> registerCommandsEvent?.dispatcher }
            .onEach { commandDispatcher -> commandDispatcher.register(node) }
            .launchIn(mainScope)
    }
}
