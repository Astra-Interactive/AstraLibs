package ru.astrainteractive.astralibs.command.api.exception

sealed class BukkitCommandException(message: String) : CommandException(message) {
    class NoPlayerException(
        name: String
    ) : CommandException("Player $name not found")

    class NoPotionEffectTypeException(
        name: String
    ) : CommandException("PotionEffectType $name not found")
}
