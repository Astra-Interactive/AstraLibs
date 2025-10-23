package ru.astrainteractive.astralibs.command.api.registrar

import com.mojang.brigadier.tree.LiteralCommandNode

interface CommandRegistrarContext<T> {
    fun registerWhenReady(node: LiteralCommandNode<T>)
}
