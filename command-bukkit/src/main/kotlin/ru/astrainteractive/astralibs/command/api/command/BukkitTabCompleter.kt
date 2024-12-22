package ru.astrainteractive.astralibs.command.api.command

import ru.astrainteractive.astralibs.command.api.context.BukkitCommandContext

/**
 * Can be used for testing purposes
 */
fun interface BukkitTabCompleter {
    fun onTabComplete(ctx: BukkitCommandContext): List<String>
}
