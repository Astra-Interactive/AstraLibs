package ru.astrainteractive.astralibs.server

import net.kyori.adventure.text.Component

fun interface KAudience {
    fun sendMessage(component: Component)
}
