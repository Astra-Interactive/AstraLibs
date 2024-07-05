package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.klibs.kstorage.api.Krate
import ru.astrainteractive.klibs.kstorage.api.StateFlowKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultStateFlowMutableKrate

object BukkitMutableStorageValue {
    inline fun <reified T> FileConfiguration.anyStateFlowMutableStorageValue(
        path: String
    ): StateFlowKrate.Mutable<T?> {
        return DefaultStateFlowMutableKrate(
            factory = { null },
            saver = { set(path, it) },
            loader = { getObject(path, T::class.java).takeIf { contains(path) } }
        )
    }

    inline fun <reified T> FileConfiguration.anyMutableStorageValue(
        path: String
    ): Krate.Mutable<T?> {
        return DefaultMutableKrate(
            factory = { null },
            saver = { set(path, it) },
            loader = { getObject(path, T::class.java).takeIf { contains(path) } }
        )
    }
}
