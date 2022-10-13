package ru.astrainteractive.astralibs.utils

import ru.astrainteractive.astralibs.AstraLibs
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import java.util.*

object AstraLibsExtensions {
    fun ItemStack.setDisplayName(name: String) {
        val meta = itemMeta
        meta?.setDisplayName(name.HEX())
        itemMeta = meta
    }
    fun ConfigurationSection.getDoubleOrNull(path:String): Double? =
        if (!this.contains(path))
            null
        else getDouble(path)
    fun ItemMeta.addAttribute(attr: Attribute, amount: Double, vararg slot: EquipmentSlot?) {
        slot.forEach {
            addAttributeModifier(
                attr,
                AttributeModifier(UUID.randomUUID(), attr.name, amount, AttributeModifier.Operation.ADD_NUMBER, it)
            )
        }
    }
    fun <T,Z> ItemMeta.setPersistentDataType(b: BukkitConstant<T, Z>, value:Z){
        persistentDataContainer.set(
            b.value,
            b.dataType,
            value?:return
        )
    }

    fun <T,Z> ItemMeta?.getPersistentData(b: BukkitConstant<T, Z>)=
        this?.persistentDataContainer?.get(
            b.value,
            b.dataType
        )

    fun <T,Z> ItemMeta?.hasPersistentData(b: BukkitConstant<T, Z>)=
        this?.persistentDataContainer?.has(
            b.value,
            b.dataType
        )

    infix fun <T> Boolean.then(param: T): T? = if (this) param else null
}

data class BukkitConstant<T, Z>(
    val value: NamespacedKey,
    val dataType: PersistentDataType<T, Z>
) {
    constructor(key: String, dataType: PersistentDataType<T, Z>) : this(
        NamespacedKey(AstraLibs.instance, key),
        dataType
    )
}