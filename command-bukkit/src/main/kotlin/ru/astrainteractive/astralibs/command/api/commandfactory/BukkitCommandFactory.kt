package ru.astrainteractive.astralibs.command.api.commandfactory

import ru.astrainteractive.astralibs.command.api.context.BukkitCommandContext

object BukkitCommandFactory : CommandFactory<BukkitCommandContext> by DefaultCommandFactory()
