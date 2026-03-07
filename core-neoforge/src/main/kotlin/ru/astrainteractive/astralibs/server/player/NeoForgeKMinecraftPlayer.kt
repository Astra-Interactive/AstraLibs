package ru.astrainteractive.astralibs.server.player

import net.minecraft.server.level.ServerPlayer
import ru.astrainteractive.astralibs.server.Audience
import ru.astrainteractive.astralibs.server.Locatable
import ru.astrainteractive.astralibs.server.Teleportable
import ru.astrainteractive.astralibs.server.util.asAudience
import ru.astrainteractive.astralibs.server.util.asLocatable
import ru.astrainteractive.astralibs.server.util.asTeleportable

class NeoForgeKMinecraftPlayer(instance: ServerPlayer) : KMinecraftPlayer,
    Audience by instance.asAudience(),
    Locatable by instance.asLocatable(),
    Teleportable by instance.asTeleportable()