package ru.astrainteractive.astralibs.util

import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

internal val CommandSourceStack.publicSource: CommandSource
    get() = this.privateField(fieldName = "source")

internal val ServerPlayer.publicServer: MinecraftServer?
    get() = this.privateField(fieldName = "server", onError = { null })
