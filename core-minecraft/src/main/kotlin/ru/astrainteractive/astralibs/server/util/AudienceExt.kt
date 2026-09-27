package ru.astrainteractive.astralibs.server.util

import net.kyori.adventure.text.Component
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.localization.locale.MinecraftLocaleSerializer
import ru.astrainteractive.astralibs.server.KAudience
import java.util.Locale

/** [Locale.ROOT] while the client has not sent its settings yet. */
private val ServerPlayer.clientLocale: Locale
    get() = MinecraftLocaleSerializer.parse(clientInformation().language()) ?: Locale.ROOT

/**
 * Adapts this [ServerPlayer] as a [KAudience] in the player's client language.
 *
 * Messages are shown in the chat area, not the action bar.
 */
fun ServerPlayer.asKAudience(): KAudience = object : KAudience {
    override val locale: Locale
        get() = clientLocale

    override fun sendMessage(component: Component) {
        displayClientMessage(component.toNative(), false)
    }
}

/** Adapts this [CommandSourceStack] as a [KAudience]: a player's source has the player's language, others none. */
fun CommandSourceStack.asKAudience(): KAudience = object : KAudience {
    override val locale: Locale
        get() = player?.clientLocale ?: Locale.ROOT

    override fun sendMessage(component: Component) {
        sendSystemMessage(component.toNative())
    }
}

/** Adapts this [MinecraftServer] console as a [KAudience]; the console has no client language. */
fun MinecraftServer.asKAudience(): KAudience = object : KAudience {
    override val locale: Locale = Locale.ROOT

    override fun sendMessage(component: Component) {
        sendSystemMessage(component.toNative())
    }
}

/** Adapts this [RconConsoleSource] as a [KAudience]; a remote console has no client language. */
fun RconConsoleSource.asKAudience(): KAudience = object : KAudience {
    override val locale: Locale = Locale.ROOT

    override fun sendMessage(component: Component) {
        sendSystemMessage(component.toNative())
    }
}
