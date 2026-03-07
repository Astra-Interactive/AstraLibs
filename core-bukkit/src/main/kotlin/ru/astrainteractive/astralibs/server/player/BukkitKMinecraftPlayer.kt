package ru.astrainteractive.astralibs.server.player

import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.server.Audience
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.util.asAudience
import ru.astrainteractive.astralibs.server.util.asLocatable
import ru.astrainteractive.astralibs.server.util.asTeleportable

class BukkitKMinecraftPlayer(val instance: Player) : KMinecraftPlayer,
    Audience by instance.asAudience(),
    Locatable by instance.asLocatable(),
    Teleportable by instance.asTeleportable()