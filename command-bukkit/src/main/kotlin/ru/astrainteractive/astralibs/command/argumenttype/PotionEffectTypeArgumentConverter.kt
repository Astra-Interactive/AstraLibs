package ru.astrainteractive.astralibs.command.argumenttype

import org.bukkit.potion.PotionEffectType
import ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter
import ru.astrainteractive.astralibs.command.model.NoPotionEffectTypeException

/**
 * Converts a string argument into a [PotionEffectType] instance.
 *
 * Resolves the effect type by name using [PotionEffectType.getByName].
 * Throws [NoPotionEffectTypeException] when the name does not match any known effect type.
 *
 * @throws NoPotionEffectTypeException if no [PotionEffectType] is registered under the given name.
 */
public data object PotionEffectTypeArgumentConverter : ArgumentConverter<PotionEffectType> {
    override fun transform(argument: String): PotionEffectType {
        return argument.let(PotionEffectType::getByName)
            ?: throw NoPotionEffectTypeException(argument)
    }
}
