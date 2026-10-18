@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.command.brigadier.util

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.command.argumenttype.api.ArgumentConverter
import ru.astrainteractive.astralibs.command.brigadier.model.BrigadierArgument
import ru.astrainteractive.astralibs.command.model.ArgumentConverterException
import ru.astrainteractive.astralibs.command.model.NoPermissionException
import ru.astrainteractive.astralibs.command.model.NotPlayerExecutorException
import ru.astrainteractive.astralibs.server.permission.api.Permission

/** Convenience alias for the Paper Brigadier [io.papermc.paper.command.brigadier.CommandSourceStack]. */
public typealias CommandSourceStack = io.papermc.paper.command.brigadier.CommandSourceStack

/** Appends a child literal sub-command to this [LiteralArgumentBuilder]. */
public fun LiteralArgumentBuilder<CommandSourceStack>.literal(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
) {
    val literal = Commands.literal(alias)
    literal.block()
    this.then(literal)
}

/** Creates a root [LiteralArgumentBuilder] for a Paper Brigadier command. */
public fun command(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
): LiteralArgumentBuilder<CommandSourceStack> {
    val literal = Commands.literal(alias)
    literal.block()
    return literal
}

/** Appends a typed required argument to this [LiteralArgumentBuilder], inferring [T] via reification. */
public inline fun <reified T : Any> LiteralArgumentBuilder<CommandSourceStack>.argument(
    alias: String,
    type: ArgumentType<T>,
    noinline block: RequiredArgumentBuilder<CommandSourceStack, T>.(BrigadierArgument<T>) -> Unit
): Unit = argument(
    alias = alias,
    type = type,
    clazz = T::class.java,
    block = block
)

/**
 * Appends a typed required argument to this [LiteralArgumentBuilder] with an explicit [clazz] token.
 * Prefer the reified overload when the type can be inferred.
 */
public fun <T : Any> LiteralArgumentBuilder<CommandSourceStack>.argument(
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

/** Appends a typed nested required argument to this [RequiredArgumentBuilder], inferring [T] via reification. */
public inline fun <reified T : Any> RequiredArgumentBuilder<CommandSourceStack, *>.argument(
    alias: String,
    type: ArgumentType<T>,
    noinline block: RequiredArgumentBuilder<CommandSourceStack, T>.(BrigadierArgument<T>) -> Unit
): Unit = argument(
    alias = alias,
    type = type,
    clazz = T::class.java,
    block = block
)

/**
 * Appends a typed nested required argument to this [RequiredArgumentBuilder] with an explicit [clazz] token.
 * Prefer the reified overload when the type can be inferred.
 */
public fun <T : Any> RequiredArgumentBuilder<CommandSourceStack, *>.argument(
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

/**
 * Attaches an execution handler to this [RequiredArgumentBuilder].
 * Exceptions from [block] are forwarded to [onFailure] rather than propagated.
 */
public fun RequiredArgumentBuilder<CommandSourceStack, *>.runs(
    onFailure: (CommandContext<CommandSourceStack>, Throwable) -> Unit = { _, _ -> },
    block: (RequiredArgumentBuilder<CommandSourceStack, *>.(CommandContext<CommandSourceStack>) -> Unit)
) {
    executes { ctx ->
        runCatching { block.invoke(this, ctx) }
            .onFailure { throwable -> onFailure.invoke(ctx, throwable) }
        Command.SINGLE_SUCCESS
    }
}

/**
 * Attaches an execution handler to this [LiteralArgumentBuilder].
 * Exceptions from [block] are forwarded to [onFailure] rather than propagated.
 */
public fun LiteralArgumentBuilder<CommandSourceStack>.runs(
    onFailure: (CommandContext<CommandSourceStack>, Throwable) -> Unit = { _, _ -> },
    block: LiteralArgumentBuilder<CommandSourceStack>.(CommandContext<CommandSourceStack>) -> Unit
) {
    executes { ctx ->
        runCatching { block.invoke(this, ctx) }
            .onFailure { throwable -> onFailure.invoke(ctx, throwable) }
        Command.SINGLE_SUCCESS
    }
}

/** Attaches a tab-completion suggestion provider to this [RequiredArgumentBuilder]. */
public fun RequiredArgumentBuilder<CommandSourceStack, *>.hints(
    block: (CommandContext<CommandSourceStack>) -> List<String>
) {
    suggests { context, builder ->
        block.invoke(context).forEach(builder::suggest)
        builder.buildFuture()
    }
}

/**
 * Asserts the sender holds [permission].
 *
 * @throws NoPermissionException if the sender lacks [permission].
 */
@Throws(NoPermissionException::class)
public fun CommandContext<CommandSourceStack>.requirePermission(permission: Permission) {
    val hasPermission = this.source.sender.hasPermission(permission.value)
    if (!hasPermission) throw NoPermissionException(permission)
}

/**
 * Returns the command executor as a [Player].
 *
 * @throws NotPlayerExecutorException if the sender is not a player.
 */
@Throws(NotPlayerExecutorException::class)
public fun CommandContext<CommandSourceStack>.requirePlayer(): Player {
    return this.source.sender as? Player
        ?: throw NotPlayerExecutorException()
}

/** Extracts a required argument value from the context using [bArgument] as the descriptor. */
@Throws(IllegalArgumentException::class)
public fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(bArgument: BrigadierArgument<T>): T {
    return getArgument(bArgument.alias, bArgument.clazz)
}

/**
 * Extracts a raw string argument and converts it to [T] via [converter].
 *
 * @throws ArgumentConverterException if the conversion fails.
 */
@Throws(ArgumentConverterException::class)
public fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(
    bArgument: BrigadierArgument<String>,
    converter: ArgumentConverter<T>
): T {
    val string = getArgument(bArgument.alias, bArgument.clazz)
    return converter.transform(string)
}
