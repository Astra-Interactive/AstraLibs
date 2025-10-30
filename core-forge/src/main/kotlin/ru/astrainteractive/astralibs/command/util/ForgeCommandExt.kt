@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.command.util

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.command.api.exception.BadArgumentException
import ru.astrainteractive.astralibs.command.api.exception.CommandException
import ru.astrainteractive.astralibs.permission.Permission
import ru.astrainteractive.astralibs.server.util.asPermissible

fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(
    alias: String,
    type: ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter<T>
): T {
    val raw = getArgument(alias, String::class.java)
    return raw?.let(type::transform) ?: throw BadArgumentException(raw, type)
}

fun <T : Any> CommandContext<CommandSourceStack>.findArgument(
    alias: String,
    type: ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter<T>
): T? {
    val raw = getArgument(alias, String::class.java)
    return runCatching { raw?.let(type::transform) }.getOrNull()
}

fun <T : Any> CommandContext<CommandSourceStack>.argumentOrElse(
    alias: String,
    type: ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter<T>,
    default: () -> T
): T {
    return findArgument(alias, type) ?: default.invoke()
}

fun CommandContext<CommandSourceStack>.requirePermission(permission: Permission): Boolean {
    if (source.source is RconConsoleSource) return true
    if (source.source is DedicatedServer) return true
    if (source.source is MinecraftServer) return true
    val serverPlayer = source.entity as? ServerPlayer ?: run {
        throw CommandException("$source.source; is not a player!")
    }
    return serverPlayer.asPermissible().hasPermission(permission)
}

fun command(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
): LiteralArgumentBuilder<CommandSourceStack?> {
    val literal = Commands.literal(alias)
    literal.block()
    return literal
}

fun LiteralArgumentBuilder<CommandSourceStack>.literal(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
) {
    val literal = Commands.literal(alias)
    literal.block()
    this.then(literal)
}

fun <T> LiteralArgumentBuilder<CommandSourceStack>.argument(
    alias: String,
    type: ArgumentType<T>,
    block: RequiredArgumentBuilder<CommandSourceStack, T>.() -> Unit
) {
    val argument = Commands.argument(alias, type)
    argument.block()
    this.then(argument)
}

fun LiteralArgumentBuilder<CommandSourceStack>.stringArgument(
    alias: String,
    block: RequiredArgumentBuilder<CommandSourceStack, String>.() -> Unit
) {
    val argument = Commands.argument(alias, StringArgumentType.string())
    argument.block()
    this.then(argument)
}

fun <T> RequiredArgumentBuilder<CommandSourceStack, *>.argument(
    alias: String,
    type: ArgumentType<T>,
    block: RequiredArgumentBuilder<CommandSourceStack, T>.() -> Unit
) {
    val argument = Commands.argument(alias, type)
    argument.block()
    this.then(argument)
}

fun RequiredArgumentBuilder<CommandSourceStack, *>.stringArgument(
    alias: String,
    block: RequiredArgumentBuilder<CommandSourceStack, String>.() -> Unit
) {
    val argument = Commands.argument(alias, StringArgumentType.string())
    argument.block()
    this.then(argument)
}

fun RequiredArgumentBuilder<CommandSourceStack, *>.runs(
    block: (RequiredArgumentBuilder<CommandSourceStack, *>.(CommandContext<CommandSourceStack>) -> Unit)
) {
    executes {
        block.invoke(this, it)
        Command.SINGLE_SUCCESS
    }
}

fun LiteralArgumentBuilder<CommandSourceStack>.runs(
    block: LiteralArgumentBuilder<CommandSourceStack>.(CommandContext<CommandSourceStack>) -> Unit
) {
    executes {
        block.invoke(this, it)
        Command.SINGLE_SUCCESS
    }
}

fun RequiredArgumentBuilder<CommandSourceStack, *>.hints(block: (CommandContext<CommandSourceStack>) -> List<String>) {
    suggests { context, builder ->
        block.invoke(context).forEach(builder::suggest)
        builder.buildFuture()
    }
}

fun RequiredArgumentBuilder<CommandSourceStack, *>.hints(list: List<String>) {
    suggests { context, builder ->
        list.forEach(builder::suggest)
        builder.buildFuture()
    }
}
