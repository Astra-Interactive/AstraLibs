package com.astrainteractive.astralibs.utils

import com.astrainteractive.astralibs.AstraLibs
import net.md_5.bungee.api.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.regex.Pattern
import kotlin.random.Random


/**
 * Simple replacement for CommandManager
 */
fun AstraLibs.registerCommand(
    alias: String,
    permission: String? = null,
    callback: (CommandSender, args: Array<out String>) -> Unit,
) =
    instance.getCommand(alias)?.setExecutor { sender, command, label, args ->
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
    callback: (CommandSender, args: Array<out String>) -> List<String>,
) =
    instance.getCommand(alias)?.setTabCompleter { commandSender, command, s, strings ->
        return@setTabCompleter callback(commandSender, strings)
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
 * Get double or null from ConfigurationSection
 */
fun ConfigurationSection.getDoubleOrNull(path: String): Double? =
    if (!this.contains(path))
        null
    else getDouble(path)

/**
 * Converting string from file configuration to hex with default param
 */
fun ConfigurationSection.getHEXString(path: String, def: String): String {
    return convertHex(getString(path, def)!!)
}

/**
 * Converting string from file configuration to hex without default param
 */
fun FileConfiguration.getHEXString(path: String): String? {
    return convertHex(getString(path))
}

/**
 * Converting string list from file configuration to hex without default param
 */
fun ConfigurationSection.getHEXStringList(path: String): List<String> {
    return convertHex(getStringList(path))
}

/**
 * Converting string to hex
 */
fun String.HEX(): String {
    return convertHex(this)
}

/**
 * Return hex string from path
 */
fun FileConfiguration.getHEXString(path: String, def: String): String {
    return convertHex(getString(path, def)!!)
}

/**
 * If you have list with entries {"entry","ementry","emementry"} and entry="me", you'll have returned list {"ementry","emementry"}.
 *
 * Very useful for TabCompleter
 *
 */
fun List<String>.withEntry(entry: String?, ignoreCase: Boolean = true): List<String> {
    return this.filter { it.contains(entry ?: "", ignoreCase = true) }
}

private val hexPattern =
    Pattern.compile("#[a-fA-F0-9]{6}|&#[a-fA-F0-9]{6}")

/**
 * Convert list of string to HEX list
 */
@JvmName("convertHexFromNullableList")
fun convertHex(list: List<String>?): List<String> {
    return list?.map { convertHex(it) } ?: listOf()
}

/**
 * Convert list of string to HEX list
 */
fun convertHex(list: MutableList<String>?): List<String> {
    return list?.map { convertHex(it) } ?: listOf()
}

/**
 * Convert string to HEX #FFFFFF pattern
 */
@JvmName("convertHexFromNullableString")
fun convertHex(line: String?): String? {
    return convertHex(line ?: return null)
}

/**
 * Convert string to HEX #FFFFFF pattern
 */
fun convertHex(l: String): String {
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


/**
 * Easy uuid get from player
 */
val Player.uuid: String
    get() = this.uniqueId.toString()


fun ItemStack.editMeta(metaBuilder: (ItemMeta) -> Unit) {
    val meta = itemMeta ?: return
    metaBuilder(meta)
    this.itemMeta = meta
}

fun Inventory.close() {
    this.viewers?.toList()?.forEach {
        it?.closeInventory()
    }
}

val OfflinePlayer.uuid: String
    get() = uniqueId.toString()

val randomColor: org.bukkit.ChatColor
    get() = org.bukkit.ChatColor.values()[Random.nextInt(org.bukkit.ChatColor.values().size)]

fun ItemStack.setDisplayName(name: String) {
    val meta = itemMeta!!
    meta.setDisplayName(name)
    itemMeta = meta
}

fun <T> serializeBukkitItem(
    obj: T,
    objectOutputStreamCreator: (ByteArrayOutputStream) -> ObjectOutputStream = { ObjectOutputStream(it) }
): ByteArray = ReflectionUtil.serializeItem(obj) {
    BukkitObjectOutputStream(it)
}

fun <T> deserializeBukkitItem(
    byteArray: ByteArray,
): T = ReflectionUtil.deserializeItem(byteArray) {
    BukkitObjectInputStream(it)
}