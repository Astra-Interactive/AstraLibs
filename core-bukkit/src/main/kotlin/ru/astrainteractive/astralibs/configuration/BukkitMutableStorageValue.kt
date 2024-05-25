package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.klibs.kstorage.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.StateFlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.StateFlowMutableStorageValue

object BukkitMutableStorageValue {
    inline fun <reified T> FileConfiguration.anyStateFlowMutableStorageValue(
        path: String
    ): StateFlowMutableStorageValue<T?> {
        return StateFlowMutableStorageValue(
            factory = { null },
            saver = { set(path, it) },
            loader = { getObject(path, T::class.java).takeIf { contains(path) } }
        )
    }

    inline fun <reified T> FileConfiguration.anyMutableStorageValue(
        path: String
    ): MutableStorageValue<T?> {
        return MutableStorageValue(
            factory = { null },
            saver = { set(path, it) },
            loader = { getObject(path, T::class.java).takeIf { contains(path) } }
        )
    }
}
