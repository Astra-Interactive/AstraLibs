package ru.astrainteractive.astralibs.configuration

import org.bukkit.configuration.file.FileConfiguration
import ru.astrainteractive.klibs.kstorage.MutableStorageValue
import ru.astrainteractive.klibs.kstorage.StateFlowMutableStorageValue
import ru.astrainteractive.klibs.kstorage.api.MutableStorageValue

object BukkitMutableStorageValue {
    inline fun <reified T> FileConfiguration.anyMutableStorageValue(path: String): MutableStorageValue<T?> {
        return StateFlowMutableStorageValue(
            default = null,
            saveSettingsValue = { set(path, it) },
            loadSettingsValue = { getObject(path, T::class.java).takeIf { contains(path) } }
        )
    }
}
