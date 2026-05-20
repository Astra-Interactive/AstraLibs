@file:Suppress("TooManyFunctions")

package ru.astrainteractive.astralibs.command.brigadier.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.rcon.RconConsoleSource
import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import ru.astrainteractive.astralibs.command.api.exception.CommandException
import ru.astrainteractive.astralibs.command.api.exception.NoPermissionException
import ru.astrainteractive.astralibs.command.api.exception.NotPlayerExecutorException
import ru.astrainteractive.astralibs.server.permission.Permission
import ru.astrainteractive.astralibs.server.util.asPermissible
import ru.astrainteractive.astralibs.util.publicSource

/** Creates a root [LiteralArgumentBuilder] for [CommandSourceStack] with the given [alias]. */
fun command(
    alias: String,
    block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
): LiteralArgumentBuilder<CommandSourceStack> {
    val literal = Commands.literal(alias)
    literal.block()
    return literal
}

/** Appends a child sub-command literal with [alias] to this builder. */
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

/** Appends a typed required argument to this [LiteralArgumentBuilder], inferring [T] from the reified type. */
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
 * Appends a typed required argument to this [LiteralArgumentBuilder] using an explicit [clazz] token.
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

/** Appends a typed nested required argument to this [RequiredArgumentBuilder], inferring [T] from the reified type. */
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
 * Appends a typed nested required argument to this [RequiredArgumentBuilder] using an explicit [clazz] token.
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
 * Registers an execution handler on this [RequiredArgumentBuilder].
 * Exceptions from [block] are forwarded to [onFailure] rather than propagated.
 *
 * @param onFailure Called when [block] throws; defaults to a no-op.
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
 * Registers an execution handler on this [LiteralArgumentBuilder].
 * Exceptions from [block] are forwarded to [onFailure] rather than propagated.
 *
 * @param onFailure Called when [block] throws; defaults to a no-op.
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

/** Registers tab-completion suggestions on this [RequiredArgumentBuilder]. */
fun RequiredArgumentBuilder<CommandSourceStack, *>.hints(block: (CommandContext<CommandSourceStack>) -> List<String>) {
    suggests { context, builder ->
        block.invoke(context).forEach(builder::suggest)
        builder.buildFuture()
    }
}

/**
 * Asserts the command source holds [permission].
 * Server and RCON sources bypass the check; unknown source types throw [CommandException].
 *
 * @throws NoPermissionException If the player lacks [permission].
 * @throws CommandException If the source is not a recognisable sender type.
 */
@Throws(NoPermissionException::class)
fun CommandContext<CommandSourceStack>.requirePermission(permission: Permission) {
    val source = source.publicSource
    if (source is RconConsoleSource) return
    if (source is DedicatedServer) return
    if (source is MinecraftServer) return
    val serverPlayer = source as? ServerPlayer ?: run {
        throw CommandException("$source; is not a player!")
    }
    val hasPermission = serverPlayer.asPermissible().hasPermission(permission)
    if (!hasPermission) throw NoPermissionException(permission)
}

/**
 * Returns the executing [ServerPlayer].
 * @throws NotPlayerExecutorException If the source is not a player.
 */
@Throws(NotPlayerExecutorException::class)
fun CommandContext<CommandSourceStack>.requirePlayer(): ServerPlayer {
    return this.source.player ?: throw NotPlayerExecutorException()
}

/** Extracts a required argument value from the context using [bArgument] as the descriptor. */
@Throws(IllegalArgumentException::class)
fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(bArgument: BrigadierArgument<T>): T {
    return getArgument(bArgument.alias, bArgument.clazz)
}

/**
 * Extracts a raw `String` argument and converts it to [T] via [converter].
 * @throws ArgumentConverterException If the conversion fails.
 */
@Throws(ArgumentConverterException::class)
fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(
    bArgument: BrigadierArgument<String>,
    converter: ArgumentConverter<T>
): T {
    val string = getArgument(bArgument.alias, bArgument.clazz)
    return converter.transform(string)
}
