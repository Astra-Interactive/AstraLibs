package ru.astrainteractive.astralibs.command.api.argumenttype

import org.bukkit.potion.PotionEffectType
import ru.astrainteractive.astralibs.command.api.exception.NoPotionEffectTypeException

/**
 * Converts a string argument into a [PotionEffectType] instance.
 *
 * Resolves the effect type by name using [PotionEffectType.getByName].
 * Throws [NoPotionEffectTypeException] when the name does not match any known effect type.
 *
 * @throws NoPotionEffectTypeException if no [PotionEffectType] is registered under the given name.
 */
data object PotionEffectTypeArgumentConverter : ArgumentConverter<PotionEffectType> {
    override fun transform(argument: String): PotionEffectType {
        return argument.let(PotionEffectType::getByName)
            ?: throw NoPotionEffectTypeException(argument)
    }
}
