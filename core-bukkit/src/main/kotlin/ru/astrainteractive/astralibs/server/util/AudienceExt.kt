package ru.astrainteractive.astralibs.server.util

import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.server.Audience
import ru.astrainteractive.astralibs.server.player.OnlineMinecraftPlayer

fun OnlineMinecraftPlayer.asAudience() = Audience { component ->
    Bukkit.getPlayer(uuid)?.sendMessage(component)
}
