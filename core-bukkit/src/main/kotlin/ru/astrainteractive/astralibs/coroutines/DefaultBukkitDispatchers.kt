package ru.astrainteractive.astralibs.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.MainCoroutineDispatcher
import org.bukkit.plugin.Plugin
import ru.astrainteractive.klibs.mikro.core.dispatchers.DefaultKotlinDispatchers
import ru.astrainteractive.klibs.mikro.core.dispatchers.KotlinDispatchers

/**
 * Bukkit dispatchers default implementation
 */
class DefaultBukkitDispatchers(plugin: Plugin) : BukkitDispatchers, KotlinDispatchers by DefaultKotlinDispatchers {
    override val BukkitMain: MainCoroutineDispatcher = BukkitMainDispatcher(plugin)
    override val BukkitAsync: CoroutineDispatcher = BukkitAsyncDispatcher(plugin)
    override val Main: MainCoroutineDispatcher
        get() = BukkitMain
}
