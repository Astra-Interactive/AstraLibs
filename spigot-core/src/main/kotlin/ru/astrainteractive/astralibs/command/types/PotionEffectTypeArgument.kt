package ru.astrainteractive.astralibs.command.types

import org.bukkit.potion.PotionEffectType

data object PotionEffectTypeArgument : ArgumentType<PotionEffectType?> {
    override fun transform(value: String?): PotionEffectType? {
        return value?.let(PotionEffectType::getByName)
    }
}
