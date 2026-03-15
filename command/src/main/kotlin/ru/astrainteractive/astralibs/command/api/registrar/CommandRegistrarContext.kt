package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.builder.LiteralArgumentBuilder

interface CommandRegistrarContext {
    fun registerWhenReady(node: LiteralArgumentBuilder<*>)
}
