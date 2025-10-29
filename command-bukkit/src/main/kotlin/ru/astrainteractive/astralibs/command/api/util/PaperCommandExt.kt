@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.command.api.util

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.command.api.exception.BadArgumentException
import ru.astrainteractive.astralibs.command.api.exception.NoPermissionException
import ru.astrainteractive.astralibs.command.api.exception.NotPlayerExecutorException
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

data class BrigadierArgument<T : Any>(
    val alias: String,
    val type: ArgumentType<T>,
    val clazz: Class<T>
)

inline fun <reified T : Any> LiteralArgumentBuilder<CommandSourceStack>.argument(
    alias: String,
    type: ArgumentType<T>,
    noinline block: RequiredArgumentBuilder<CommandSourceStack, T>.(BrigadierArgument<T>) -> Unit
) = argument(
    alias = alias,
    type = type,
    clazz = T::class.java,
    block = block
)

fun <T : Any> LiteralArgumentBuilder<CommandSourceStack>.argument(
    alias: String,
    type: ArgumentType<T>,
    clazz: Class<T>,
    block: RequiredArgumentBuilder<CommandSourceStack, T>.(BrigadierArgument<T>) -> Unit
) {
    val argument = Commands.argument(alias, type)
    val brigadierArgument = BrigadierArgument(
        alias = alias,
        type = type,
        clazz = clazz
    )
    argument.block(brigadierArgument)
    this.then(argument)
}

inline fun <reified T : Any> RequiredArgumentBuilder<CommandSourceStack, *>.argument(
    alias: String,
    type: ArgumentType<T>,
    noinline block: RequiredArgumentBuilder<CommandSourceStack, T>.(BrigadierArgument<T>) -> Unit
) = argument(
    alias = alias,
    type = type,
    clazz = T::class.java,
    block = block
)

fun <T : Any> RequiredArgumentBuilder<CommandSourceStack, *>.argument(
    alias: String,
    type: ArgumentType<T>,
    clazz: Class<T>,
    block: RequiredArgumentBuilder<CommandSourceStack, T>.(BrigadierArgument<T>) -> Unit
) {
    val argument = Commands.argument(alias, type)
    val brigadierArgument = BrigadierArgument(
        alias = alias,
        type = type,
        clazz = clazz
    )
    argument.block(brigadierArgument)
    this.then(argument)
}

fun RequiredArgumentBuilder<CommandSourceStack, *>.runs(
    onFailure: (CommandContext<CommandSourceStack>, Throwable) -> Unit = { _, _ -> },
    block: (RequiredArgumentBuilder<CommandSourceStack, *>.(CommandContext<CommandSourceStack>) -> Unit)
) {
    executes { ctx ->
        runCatching { block.invoke(this, ctx) }
            .onFailure { onFailure.invoke(ctx, it) }
        Command.SINGLE_SUCCESS
    }
}

fun LiteralArgumentBuilder<CommandSourceStack>.runs(
    onFailure: (CommandContext<CommandSourceStack>, Throwable) -> Unit = { _, _ -> },
    block: LiteralArgumentBuilder<CommandSourceStack>.(CommandContext<CommandSourceStack>) -> Unit
) {
    executes { ctx ->
        runCatching { block.invoke(this, ctx) }
            .onFailure { onFailure.invoke(ctx, it) }
        Command.SINGLE_SUCCESS
    }
}

fun RequiredArgumentBuilder<CommandSourceStack, *>.hints(block: (CommandContext<CommandSourceStack>) -> List<String>) {
    suggests { context, builder ->
        block.invoke(context).forEach(builder::suggest)
        builder.buildFuture()
    }
}

@Throws(NoPermissionException::class)
fun CommandContext<CommandSourceStack>.requirePermission(permission: Permission) {
    val hasPermission = this.source.sender.hasPermission(permission.value)
    if (!hasPermission) throw NoPermissionException(permission)
}

@Throws(NotPlayerExecutorException::class)
fun CommandContext<CommandSourceStack>.requirePlayer(): Player {
    return this.source.sender as? Player
        ?: throw NotPlayerExecutorException()
}

fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(bArgument: BrigadierArgument<T>): T {
    return getArgument(bArgument.alias, bArgument.clazz)
}
