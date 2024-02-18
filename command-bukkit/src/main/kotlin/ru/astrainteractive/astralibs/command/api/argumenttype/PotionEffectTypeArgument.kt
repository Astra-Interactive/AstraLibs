package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.potion.PotionEffectType

data object PotionEffectTypeArgument : ArgumentType<PotionEffectType?> {
    override fun transform(value: String?): PotionEffectType? {
        return value?.let(PotionEffectType::getByName)
    }
}
