package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.CoroutineDispatcher
import org.bukkit.plugin.Plugin
import ru.astrainteractive.klibs.mikro.core.dispatchers.DefaultKotlinDispatchers
import ru.astrainteractive.klibs.mikro.core.dispatchers.KotlinDispatchers

/**
 * Bukkit dispatchers default implementation
 */
class DefaultBukkitDispatchers(plugin: Plugin) : BukkitDispatchers, KotlinDispatchers by DefaultKotlinDispatchers {
    override val BukkitMain: CoroutineDispatcher = BukkitMainDispatcher(plugin)
    override val BukkitAsync: CoroutineDispatcher = BukkitAsyncDispatcher(plugin)
    override val Main: CoroutineDispatcher
        get() = BukkitMain
}
