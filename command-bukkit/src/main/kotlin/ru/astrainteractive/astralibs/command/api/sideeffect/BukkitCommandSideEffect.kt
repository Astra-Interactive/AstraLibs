package ru.astrainteractive.astralibs.command.api.sideeffect

import ru.astrainteractive.astralibs.command.api.context.BukkitCommandContext

fun interface BukkitCommandSideEffect<R : Any> : CommandSideEffect<R, BukkitCommandContext>
