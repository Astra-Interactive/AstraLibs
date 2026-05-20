@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.command.api.brigadier.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.entity.Player
import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import ru.astrainteractive.astralibs.command.api.exception.NoPermissionException
import ru.astrainteractive.astralibs.command.api.exception.NotPlayerExecutorException
import ru.astrainteractive.astralibs.server.permission.Permission

/** Convenience alias for the Paper Brigadier [io.papermc.paper.command.brigadier.CommandSourceStack]. */
typealias CommandSourceStack = io.papermc.paper.command.brigadier.CommandSourceStack

/** Creates a root [LiteralArgumentBuilder] for a Paper Brigadier command. */
fun command(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
): LiteralArgumentBuilder<CommandSourceStack> {
    val literal = Commands.literal(alias)
    literal.block()
    return literal
}

/** Appends a child literal sub-command to this [LiteralArgumentBuilder]. */
fun LiteralArgumentBuilder<CommandSourceStack>.literal(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
) {
    val literal = Commands.literal(alias)
    literal.block()
    this.then(literal)
}

/** Metadata needed to register and extract a typed Brigadier argument. */
data class BrigadierArgument<T : Any>(
    val alias: String,
    val type: ArgumentType<T>,
    val clazz: Class<T>
)

/** Appends a typed required argument to this [LiteralArgumentBuilder], inferring [T] via reification. */
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

/**
 * Appends a typed required argument to this [LiteralArgumentBuilder] with an explicit [clazz] token.
 * Prefer the reified overload when the type can be inferred.
 */
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

/** Appends a typed nested required argument to this [RequiredArgumentBuilder], inferring [T] via reification. */
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

/**
 * Appends a typed nested required argument to this [RequiredArgumentBuilder] with an explicit [clazz] token.
 * Prefer the reified overload when the type can be inferred.
 */
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

/**
 * Attaches an execution handler to this [RequiredArgumentBuilder].
 * Exceptions from [block] are forwarded to [onFailure] rather than propagated.
 */
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

/**
 * Attaches an execution handler to this [LiteralArgumentBuilder].
 * Exceptions from [block] are forwarded to [onFailure] rather than propagated.
 */
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

/** Attaches a tab-completion suggestion provider to this [RequiredArgumentBuilder]. */
fun RequiredArgumentBuilder<CommandSourceStack, *>.hints(block: (CommandContext<CommandSourceStack>) -> List<String>) {
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
fun CommandContext<CommandSourceStack>.requirePermission(permission: Permission) {
    val hasPermission = this.source.sender.hasPermission(permission.value)
    if (!hasPermission) throw NoPermissionException(permission)
}

/**
 * Returns the command executor as a [Player].
 *
 * @throws NotPlayerExecutorException if the sender is not a player.
 */
@Throws(NotPlayerExecutorException::class)
fun CommandContext<CommandSourceStack>.requirePlayer(): Player {
    return this.source.sender as? Player
        ?: throw NotPlayerExecutorException()
}

/** Extracts a required argument value from the context using [bArgument] as the descriptor. */
@Throws(IllegalArgumentException::class)
fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(bArgument: BrigadierArgument<T>): T {
    return getArgument(bArgument.alias, bArgument.clazz)
}

/**
 * Extracts a raw string argument and converts it to [T] via [converter].
 *
 * @throws ArgumentConverterException if the conversion fails.
 */
@Throws(ArgumentConverterException::class)
fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(
    bArgument: BrigadierArgument<String>,
    converter: ArgumentConverter<T>
): T {
    val string = getArgument(bArgument.alias, bArgument.clazz)
    return converter.transform(string)
}
