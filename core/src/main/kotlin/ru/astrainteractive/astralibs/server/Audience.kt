package ru.astrainteractive.astralibs.server

import net.kyori.adventure.text.Component

fun interface Audience {
    fun sendMessage(component: Component)
}
