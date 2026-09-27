package ru.astrainteractive.astralibs.server.util

import net.kyori.adventure.audience.Audience
import ru.astrainteractive.astralibs.server.AdventureKAudience

/** Adapts this [Audience], e.g. a player or a command sender, as a receiver that knows its language. */
fun Audience.asKAudience(): AdventureKAudience = AdventureKAudience(this)
