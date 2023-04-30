package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin

/**
 * Bukkit dispatchers default implementation
 */
class DefaultBukkitDispatchers(plugin: Plugin) : BukkitDispatchers, KotlinDispatchers by KDispatchers {
    override val BukkitMain: CoroutineDispatcher = BukkitMainDispatcher(plugin)
    override val BukkitAsync: CoroutineDispatcher = BukkitAsyncDispatcher(plugin)
}
