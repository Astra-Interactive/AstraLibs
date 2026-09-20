package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import kotlinx.coroutines.CoroutineScope
import ru.astrainteractive.astralibs.command.api.brigadier.command.MultiplatformCommands

/**
 * Implementations defer the actual registration until the platform signals
 * it is ready to accept new commands (e.g. after the server has initialised).
 */
interface CommandRegistrarContext {
    /**
     * Keeps [node] in the server's command dispatcher until [scope] is cancelled.
     *
     * Calling this before the server is ready never throws: the node is applied as soon as the
     * platform accepts commands, re-applied whenever the platform rebuilds its command tree, and
     * removed again once [scope] dies. Passing the scope of whatever owns the command is therefore
     * all a feature toggled at runtime needs - when that scope dies, the command leaves with it.
     *
     * [node] must be built by the same platform's [MultiplatformCommands] factory: its source type
     * is not reified, so a foreign node is only rejected once the command executes and sender
     * resolution meets the wrong source class.
     */
    fun registerWhenReady(
        node: LiteralArgumentBuilder<*>,
        scope: CoroutineScope
    )
}

/**
 * Keeps every tree in [nodes] registered until [scope] is cancelled.
 *
 * @see CommandRegistrarContext.registerWhenReady
 */
fun CommandRegistrarContext.registerWhenReady(
    nodes: List<LiteralArgumentBuilder<*>>,
    scope: CoroutineScope
) {
    nodes.forEach { node -> registerWhenReady(node = node, scope = scope) }
}
