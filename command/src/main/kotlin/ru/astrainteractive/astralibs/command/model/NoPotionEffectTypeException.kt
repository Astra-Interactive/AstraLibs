package ru.astrainteractive.astralibs.command.model

/** Thrown when a potion effect type lookup yields no result. */
public class NoPotionEffectTypeException(
    name: String
) : CommandException("PotionEffectType $name not found")
