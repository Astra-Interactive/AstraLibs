@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.command.api.util

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import ru.astrainteractive.astralibs.command.api.exception.BadArgumentException
import ru.astrainteractive.astralibs.permission.Permission

fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(
    alias: String,
    type: ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentType<T>
): T {
    val raw = getArgument(alias, String::class.java)
    return raw?.let(type::transform) ?: throw BadArgumentException(raw, type)
}

fun <T : Any> CommandContext<CommandSourceStack>.findArgument(
    alias: String,
    type: ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentType<T>
): T? {
    val raw = getArgument(alias, String::class.java)
    return runCatching { raw?.let(type::transform) }.getOrNull()
}

fun <T : Any> CommandContext<CommandSourceStack>.argumentOrElse(
    alias: String,
    type: ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentType<T>,
    default: () -> T
): T {
    return findArgument(alias, type) ?: default.invoke()
}

fun CommandContext<CommandSourceStack>.requirePermission(permission: Permission): Boolean {
    return this.source.sender.hasPermission(permission.value)
}

fun command(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
): LiteralArgumentBuilder<CommandSourceStack> {
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

fun <T : Any> LiteralArgumentBuilder<CommandSourceStack>.argument(
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

fun <T : Any> RequiredArgumentBuilder<CommandSourceStack, *>.argument(
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
