@file:Suppress("TooManyFunctions", "Filename")

package ru.astrainteractive.astralibs.utils

import net.md_5.bungee.api.ChatColor
import org.bukkit.OfflinePlayer
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.Plugin
import ru.astrainteractive.astralibs.logging.JUtilLogger
import ru.astrainteractive.astralibs.logging.Logger
import java.io.File
import java.util.UUID
import java.util.regex.Pattern
import kotlin.random.Random

/**
 * Setup bukkit logger
 */
fun Logger.Companion.buildWithSpigot(tag: String, instance: Plugin): Logger {
    return JUtilLogger(
        tag = tag,
        folder = File(instance.dataFolder, "logs").also { it.mkdirs() },
        logger = instance.logger
    )
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
    if (!this.contains(path)) {
        null
    } else {
        getDouble(path)
    }

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
fun String.hex(): String {
    return convertHex(this)
}

/**
 * Return hex string from path
 */
fun FileConfiguration.getHEXString(path: String, def: String): String {
    return convertHex(getString(path, def)!!)
}

/**
 * If you have list with entries {"entry","ementry","emementry"} and entry="me",
 * you'll have returned list {"ementry","emementry"}.
 *
 * Very useful for TabCompleter
 *
 */
fun List<String>.withEntry(entry: String?, ignoreCase: Boolean = true): List<String> {
    return this.filter { it.contains(entry ?: "", ignoreCase = ignoreCase) }
}

private val hexPattern = Pattern.compile("#[a-fA-F0-9]{6}|&#[a-fA-F0-9]{6}")

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
            color,
            ChatColor.of(
                if (color.startsWith("&")) color.substring(1) else color
            ).toString() + ""
        )
        match = hexPattern.matcher(line)
    }
    return org.bukkit.ChatColor.translateAlternateColorCodes('&', line)
}

fun ItemMeta.addAttribute(attr: Attribute, amount: Double, vararg slot: EquipmentSlot?) {
    slot.forEach {
        addAttributeModifier(
            attr,
            AttributeModifier(UUID.randomUUID(), attr.name, amount, AttributeModifier.Operation.ADD_NUMBER, it)
        )
    }
}

/**
 * Easy uuid get from player
 */
val OfflinePlayer.uuid: String
    get() = uniqueId.toString()

val randomColor: org.bukkit.ChatColor
    get() = org.bukkit.ChatColor.values()[Random.nextInt(org.bukkit.ChatColor.values().size)]
