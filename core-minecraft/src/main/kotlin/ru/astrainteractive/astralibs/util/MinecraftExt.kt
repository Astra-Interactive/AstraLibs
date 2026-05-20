package ru.astrainteractive.astralibs.util

import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

/** Exposes the private `source` field of [CommandSourceStack] for sender-type inspection. */
internal val CommandSourceStack.publicSource: CommandSource
    get() = this.privateField(fieldName = "source")

/** Exposes the private `server` field of [ServerPlayer]; returns `null` on reflective error. */
internal val ServerPlayer.publicServer: MinecraftServer?
    get() = this.privateField(fieldName = "server", onError = { null })
