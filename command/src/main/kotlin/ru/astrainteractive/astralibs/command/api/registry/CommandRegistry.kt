package ru.astrainteractive.astralibs.command.api.registry

import ru.astrainteractive.astralibs.command.api.command.Command
import ru.astrainteractive.astralibs.command.api.context.CommandContext

interface CommandRegistry<RC : CommandRegistryContext, C : Command<out CommandContext>> {
    fun register(command: C, registryContext: RC)
}
