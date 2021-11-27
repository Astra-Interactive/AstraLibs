package com.astrainteractive.astralibs

import net.md_5.bungee.api.ChatColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.plugin.IllegalPluginAccessException
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.Future
import java.util.regex.Pattern


/**
 * Catching errors. Return null if Exception happened
 */
inline fun <T> catching(block: () -> T?): T? {
    return try {
        val result = block()
        result
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

/**
 * Returns value of enum or null if value not found
 */
inline fun <reified T : Enum<T>> valueOfOrNull(type: String): T? =
    catchingNoStackTrace {
        java.lang.Enum.valueOf(T::class.java, type)
    }


/**
 * Simple replacement for CommandManager
 */
fun AstraLibs.registerCommand(
    alias: String,
    permission: String? = null,
    callback: (CommandSender, args: Array<out String>) -> Unit
) =
    AstraLibs.instance.getCommand(alias)?.setExecutor { sender, command, label, args ->
        if (permission != null && !sender.hasPermission(permission))
            return@setExecutor true
        callback(sender, args)
        return@setExecutor true
    }

/**
 * Simple replacement for TabCompleter
 */
fun AstraLibs.registerTabCompleter(
    alias: String,
    permission: String? = null,
    callback: (CommandSender, args: Array<out String>) -> List<String>
) =
    AstraLibs.instance.getCommand(alias)?.setTabCompleter { commandSender, command, s, strings ->
        return@setTabCompleter callback(commandSender,strings)
    }


/**
 * Catch Exception without stackTrace
 */
inline fun <T> catchingNoStackTrace(block: () -> T?): T? {
    return try {
        val result = block()
        result
    } catch (e: Throwable) {
        null
    } catch (e: Exception) {
        null
    }
}

/**
 * Get Float from ConfigurationSection
 */
fun ConfigurationSection.getFloat(path: String): Float =
    getDouble(path).toFloat()

/**
 * Get float or default value from ConfigurationSection
 */
fun ConfigurationSection.getFloat(path: String, defaultValue: Float): Float =
    getDouble(path, defaultValue.toDouble()).toFloat()



/**
 * Converting string from file configuration to hex with default param
 */
fun ConfigurationSection.getHEXString(path: String, def: String): String {
    return AstraUtils.HEXPattern(getString(path, def)!!)
}

/**
 * Converting string from file configuration to hex without default param
 */
fun FileConfiguration.getHEXString(path: String): String? {
    return AstraUtils.HEXPattern(getString(path))
}

/**
 * Converting string list from file configuration to hex without default param
 */
fun ConfigurationSection.getHEXStringList(path: String): List<String> {
    return AstraUtils.HEXPattern(getStringList(path))
}

/**
 * Converting string to hex
 */
fun String.HEX(): String {
    return AstraUtils.HEXPattern(this)
}

/**
 * Return hex string from path
 */
fun FileConfiguration.getHEXString(path: String, def: String): String {
    return AstraUtils.HEXPattern(getString(path, def)!!)
}




/**
 * If you have list with entries {"entry","ementry","emementry"} and entry="me", you'll have returned list {"ementry","emementry"}.
 *
 * Very useful for TabCompleter
 *
 */
fun List<String>.withEntry(entry: String?, ignoreCase: Boolean = true): List<String> {
    val list = mutableListOf<String>()
    for (line in this)
        if (line.contains(entry ?: "", ignoreCase = true))
            list.add(line)
    return list
}

/**
 * Creates async Bukkit task
 */
fun runAsyncTask(function: Runnable): BukkitTask? {
    return try {
        val id = System.currentTimeMillis()
        val taskRef = Bukkit.getScheduler().runTaskAsynchronously(AstraLibs.instance, Runnable {
            function.run()
            AstraLibs.onBukkitTaskEnded(id)
        })
        AstraLibs.onBukkitTaskAdded(id, taskRef)
        return taskRef
    } catch (e: IllegalPluginAccessException) {
        println("${ChatColor.RED} Trying to create thread while disabling")
        null
    }

}

/**
 * Returns you to main Thread
 */
fun callSyncMethod(function: Runnable): Future<Unit>? {

    return try {

        Bukkit.getScheduler().callSyncMethod(AstraLibs.instance) {
            function.run()
        }
    } catch (e: IllegalPluginAccessException) {
        println("${ChatColor.RED} Trying to create sync method while disabling")
        return null
    }
}



/**
 * Utils class
 */
object AstraUtils {
    private val hexPattern =
        Pattern.compile("#[a-fA-F0-9]{6}|&#[a-fA-F0-9]{6}")

    /**
     * Convert list to HEX list
     */
    @JvmName("HEXPattern1")
    fun HEXPattern(_list: List<String>?): List<String> {
        val list = _list?.toMutableList() ?: return mutableListOf()
        for (i in list.indices) list[i] = HEXPattern(list[i])
        return list
    }
    /**
     * Convert list to HEX list
     */
    fun HEXPattern(list: MutableList<String>?): List<String> {
        list ?: return mutableListOf()
        for (i in list.indices) list[i] = HEXPattern(list[i])
        return list
    }

    /**
     * Convert string to com.astrainteractive.astralibs.HEX #FFFFFF pattern
     */
    @JvmName("HEXPattern1")
    fun HEXPattern(line: String?): String? {
        line ?: return line
        return HEXPattern(line)
    }

    /**
     * Convert string to com.astrainteractive.astralibs.HEX #FFFFFF pattern
     */
    fun HEXPattern(l: String): String {
        var line = l
        var match = hexPattern.matcher(line)
        while (match.find()) {
            val color = line.substring(match.start(), match.end())
            line = line.replace(
                color, ChatColor.of(
                    if (color.startsWith("&")) color.substring(1) else color
                ).toString() + ""
            )
            match = hexPattern.matcher(line)
        }
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', line)
    }

}