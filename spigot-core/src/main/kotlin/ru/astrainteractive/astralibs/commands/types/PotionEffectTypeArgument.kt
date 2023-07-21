package ru.astrainteractive.astralibs.commands.types

import org.bukkit.potion.PotionEffectType

data object PotionEffectTypeArgument : ArgumentType<PotionEffectType> {
    override fun transform(value: String): PotionEffectType {
        return PotionEffectType.getByName(value) ?: error("Potion effect with name $value not found")
    }
}
