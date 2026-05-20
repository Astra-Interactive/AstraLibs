package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder

/**
 * Provides the ability to register a Brigadier command tree with the server.
 *
 * Implementations defer the actual registration until the platform signals
 * it is ready to accept new commands (e.g. after the server has initialised).
 */
interface CommandRegistrarContext {
    /**
     * Schedules [node] for registration with the server's command dispatcher.
     *
     * The node will be registered once the platform is in the appropriate lifecycle
     * state to accept commands. Calling this method before the server is ready must
     * not throw; the implementation is expected to queue the node internally.
     *
     * @param node The root [LiteralArgumentBuilder] representing the command tree to register.
     */
    fun registerWhenReady(node: LiteralArgumentBuilder<*>)
}
