package ru.astrainteractive.astralibs.command.api.exception

class NoPlayerException(name: String) : CommandException("Player $name not found")
