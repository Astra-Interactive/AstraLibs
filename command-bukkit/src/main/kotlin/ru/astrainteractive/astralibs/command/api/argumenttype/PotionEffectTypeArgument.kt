package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.potion.PotionEffectType
import ru.astrainteractive.astralibs.command.api.exception.NoPotionEffectTypeException

data object PotionEffectTypeArgument : ArgumentType<PotionEffectType> {
    override val key: String = "PotionEffectTypeArgument"
    override fun transform(value: String): PotionEffectType {
        return value.let(PotionEffectType::getByName)
            ?: throw NoPotionEffectTypeException(value)
    }
}
