package ru.astrainteractive.astralibs.command.model

/** Thrown when a player lookup yields no result. */
public class NoPlayerException(
    name: String
) : CommandException("Player $name not found")
