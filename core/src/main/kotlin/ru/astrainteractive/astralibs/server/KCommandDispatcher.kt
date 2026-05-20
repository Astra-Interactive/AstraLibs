package ru.astrainteractive.astralibs.server

/** Platform-agnostic command dispatcher. Bridges Bukkit/NeoForge command APIs. */
fun interface KCommandDispatcher {
    /** Dispatches [command] without a leading slash as if typed by this entity. */
    fun dispatchCommand(command: String)
}
