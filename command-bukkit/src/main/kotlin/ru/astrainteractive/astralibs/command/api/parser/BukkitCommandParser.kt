package ru.astrainteractive.astralibs.command.api.parser

import ru.astrainteractive.astralibs.command.api.context.BukkitCommandContext

fun interface BukkitCommandParser<R : Any> : CommandParser<R, BukkitCommandContext>
