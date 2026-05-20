package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.klibs.kstorage.api.MutableKrate
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.asStateFlowMutableKrate
import ru.astrainteractive.klibs.kstorage.api.impl.DefaultMutableKrate

/** Factory helpers for [MutableKrate] and [StateFlowMutableKrate] backed by a Bukkit [FileConfiguration]. */
object BukkitMutableStorageValue {
    /**
     * Creates a [StateFlowMutableKrate] for a nullable [T] at [path] in this [FileConfiguration].
     * Emits `null` when the key is absent.
     */
    inline fun <reified T> FileConfiguration.anyStateFlowMutableStorageValue(
        path: String
    ): StateFlowMutableKrate<T?> {
        return DefaultMutableKrate(
            factory = { null },
            saver = { set(path, it) },
            loader = { getObject(path, T::class.java).takeIf { contains(path) } }
        ).asStateFlowMutableKrate()
    }

    /**
     * Creates a [MutableKrate] for a nullable [T] at [path] in this [FileConfiguration].
     * Returns `null` when the key is absent.
     */
    inline fun <reified T> FileConfiguration.anyMutableStorageValue(
        path: String
    ): MutableKrate<T?> {
        return DefaultMutableKrate(
            factory = { null },
            saver = { set(path, it) },
            loader = { getObject(path, T::class.java).takeIf { contains(path) } }
        )
    }
}
