package ru.astrainteractive.astralibs.server.api

/** Platform-agnostic command dispatcher. Bridges Bukkit/NeoForge command APIs. */
public fun interface KCommandDispatcher {
    /** Dispatches [command] without a leading slash as if typed by this entity. */
    public fun dispatchCommand(command: String)
}
