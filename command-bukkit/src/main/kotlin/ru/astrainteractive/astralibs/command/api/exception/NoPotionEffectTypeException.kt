package ru.astrainteractive.astralibs.command.api.exception

class NoPotionEffectTypeException(val name: String) : CommandException("PotionEffectType $name not found")
