package ru.astrainteractive.astralibs

import ru.astrainteractive.astralibs.utils.catching
import com.google.gson.Gson
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

@kotlinx.serialization.Serializable
data class ActionInventoriesHolder(
    val action_inventory:String
){


    companion object {
        fun getAll(): ActionInventoriesHolder? {

            return ActionInventoriesHolder("Aaa")
        }
    }
}
/**
 * Converting yaml file to .json format
 *
 * It allows you to use GSON with yaml
 */
@Deprecated("Do not use with big data. Can cause null exception. Use on your own risk")
object AstraYamlParser{
    fun <T> fixNull(v1:T?,v2:T):T = v1?:v2
    /**
     * Convert configuration section to map
     */
    fun configurationSectionToMap(section: ConfigurationSection?): Map<String, Any?> {
        section ?: return hashMapOf<String, Any>()
        var s = section.getKeys(false).associateWith { key ->
            val value: Any? = if (section.isConfigurationSection(key))
                configurationSectionToMap(
                    section.getConfigurationSection(
                        key
                    )
                )
            else {
                val obj = section.get(key)
                if (obj is String)
                    "\"${obj}\""
                else if (obj is List<*>) {
                    if (obj.firstOrNull() is String)
                        obj.map { "\"${it}\"" }
                    else obj
                }
                else obj
            }
            value
        }
        return s
    }

    /**
     * Convert configuration section to Class
     */
    inline fun <reified T> configurationSectionToClass(section: ConfigurationSection?,debugSection:Boolean=false): T? = catching {
        val map = section.toMap() ?: return null

        val stringMap = map.toString()
        if (debugSection) {
            println(map)
            println(stringMap)
        }

        return Gson().fromJson(stringMap, T::class.java)
    }

    /**
     * Convert configuration section to map
     */
    fun ConfigurationSection?.toMap() =
        configurationSectionToMap(this)


    /**
     * Convert file configuration to Class
     */
    inline fun <reified T> fileConfigurationToClass(file: FileConfiguration?): T? =
        configurationSectionToClass(file?.defaultSection)

    /**
     * Convert fileConfig to map
     */
    fun mapFromFileConfig(f: FileConfiguration?) =
        f?.defaultSection?.toMap()


}