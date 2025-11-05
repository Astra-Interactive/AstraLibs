package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.potion.PotionEffectType
import ru.astrainteractive.astralibs.command.api.exception.NoPotionEffectTypeException

data object PotionEffectTypeArgumentConverter : ArgumentConverter<PotionEffectType> {
    override fun transform(argument: String): PotionEffectType {
        return argument.let(PotionEffectType::getByName)
            ?: throw NoPotionEffectTypeException(argument)
    }
}
