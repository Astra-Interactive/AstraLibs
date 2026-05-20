package ru.astrainteractive.astralibs.command.api.brigadier.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.command.api.brigadier.sender.ConsoleKCommandSender
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KCommandSender
import ru.astrainteractive.astralibs.command.api.brigadier.sender.KPlayerKCommandSender
import ru.astrainteractive.astralibs.command.api.exception.ArgumentConverterException
import ru.astrainteractive.astralibs.command.api.exception.NoPermissionException
import ru.astrainteractive.astralibs.command.api.exception.NotPlayerExecutorException
import ru.astrainteractive.astralibs.server.permission.Permission
import ru.astrainteractive.astralibs.server.player.OnlineKPlayer

/**
 * Brigadier DSL for building platform-agnostic command trees.
 *
 * @param commands Platform-specific factory for brigadier builders and sender resolution.
 */
@Suppress("TooManyFunctions", "MaximumLineLength", "MaxLineLength")
class MultiplatformCommand(private val commands: MultiplatformCommands) {
    /** Creates a root literal command node and applies [block] to it. */
    fun command(
        alias: String,
        block: LiteralArgumentBuilder<Any>.() -> Unit
    ): LiteralArgumentBuilder<Any> {
        val literal = commands.literal(alias)
        literal.block()
        return literal
    }

    /** Appends a child literal sub-command node to this builder. */
    fun LiteralArgumentBuilder<Any>.literal(
        alias: String,
        block: LiteralArgumentBuilder<Any>.() -> Unit
    ) {
        val literal = commands.literal(alias)
        literal.block()
        this.then(literal)
    }

    /**
     * Metadata needed to retrieve a typed Brigadier argument from a [CommandContext].
     *
     * Passed into [argument] builder lambdas so callers can use [requireArgument] without
     * repeating the alias or class token.
     */
    data class BrigadierArgument<T : Any>(
        val alias: String,
        val type: ArgumentType<T>,
        val clazz: Class<T>
    )

    /** Appends a typed argument child node to this literal builder (type inferred via reification). */
    inline fun <reified T : Any> LiteralArgumentBuilder<Any>.argument(
        alias: String,
        type: ArgumentType<T>,
        noinline block: RequiredArgumentBuilder<Any, T>.(BrigadierArgument<T>) -> Unit
    ) = argument(
        alias = alias,
        type = type,
        clazz = T::class.java,
        block = block
    )

    /** Appends a typed argument child node to this literal builder with an explicit [clazz] token. */
    fun <T : Any> LiteralArgumentBuilder<Any>.argument(
        alias: String,
        type: ArgumentType<T>,
        clazz: Class<T>,
        block: RequiredArgumentBuilder<Any, T>.(BrigadierArgument<T>) -> Unit
    ) {
        val argument = commands.argument(alias, type)
        val brigadierArgument = BrigadierArgument(
            alias = alias,
            type = type,
            clazz = clazz
        )
        argument.block(brigadierArgument)
        this.then(argument)
    }

    /** Appends a typed argument child node to this required-argument builder (type inferred via reification). */
    inline fun <reified T : Any> RequiredArgumentBuilder<Any, *>.argument(
        alias: String,
        type: ArgumentType<T>,
        noinline block: RequiredArgumentBuilder<Any, T>.(BrigadierArgument<T>) -> Unit
    ) = argument(
        alias = alias,
        type = type,
        clazz = T::class.java,
        block = block
    )

    /** Appends a typed argument child node to this required-argument builder with an explicit [clazz] token. */
    fun <T : Any> RequiredArgumentBuilder<Any, *>.argument(
        alias: String,
        type: ArgumentType<T>,
        clazz: Class<T>,
        block: RequiredArgumentBuilder<Any, T>.(BrigadierArgument<T>) -> Unit
    ) {
        val argument = commands.argument(alias, type)
        val brigadierArgument = BrigadierArgument(
            alias = alias,
            type = type,
            clazz = clazz
        )
        argument.block(brigadierArgument)
        this.then(argument)
    }

    /**
     * Attaches an execution handler to this required-argument node.
     *
     * Exceptions are forwarded to [onFailure] rather than propagating to the Brigadier dispatcher.
     */
    fun RequiredArgumentBuilder<Any, *>.runs(
        onFailure: (CommandContext<Any>, Throwable) -> Unit = { _, _ -> },
        block: (RequiredArgumentBuilder<Any, *>.(CommandContext<Any>) -> Unit)
    ) {
        executes { ctx ->
            runCatching { block.invoke(this, ctx) }
                .onFailure { onFailure.invoke(ctx, it) }
            Command.SINGLE_SUCCESS
        }
    }

    /**
     * Attaches an execution handler to this literal node.
     *
     * Exceptions are forwarded to [onFailure] rather than propagating to the Brigadier dispatcher.
     */
    fun LiteralArgumentBuilder<Any>.runs(
        onFailure: (CommandContext<Any>, Throwable) -> Unit = { _, _ -> },
        block: LiteralArgumentBuilder<Any>.(CommandContext<Any>) -> Unit
    ) {
        executes { ctx ->
            runCatching { block.invoke(this, ctx) }
                .onFailure { onFailure.invoke(ctx, it) }
            Command.SINGLE_SUCCESS
        }
    }

    /** Registers tab-completion suggestions for this argument node. */
    fun RequiredArgumentBuilder<Any, *>.hints(block: (CommandContext<Any>) -> List<String>) {
        suggests { context, builder ->
            block.invoke(context).forEach(builder::suggest)
            builder.buildFuture()
        }
    }

    /** Retrieves the parsed value of [bArgument] from this command context. */
    @Throws(IllegalArgumentException::class)
    fun <T : Any> CommandContext<Any>.requireArgument(bArgument: BrigadierArgument<T>): T {
        return getArgument(bArgument.alias, bArgument.clazz)
    }

    /** Retrieves a raw string argument from this context and converts it via [converter]. */
    @Throws(ArgumentConverterException::class)
    fun <T : Any> CommandContext<Any>.requireArgument(
        bArgument: BrigadierArgument<String>,
        converter: ArgumentConverter<T>
    ): T {
        val string = getArgument(bArgument.alias, bArgument.clazz)
        return converter.transform(string)
    }

    /** Throws [NoPermissionException] if the sender does not hold [permission]. */
    @Throws(NoPermissionException::class)
    fun CommandContext<Any>.requirePermission(permission: Permission) {
        val sender = commands.getSender(this)
        val hasPermission = when (sender) {
            is ConsoleKCommandSender -> sender.hasPermission(permission)
            is KPlayerKCommandSender -> sender.hasPermission(permission)
        }
        if (!hasPermission) throw NoPermissionException(permission)
    }

    /** Returns the executing [OnlineKPlayer], or throws [NotPlayerExecutorException] if the sender is console. */
    @Throws(NotPlayerExecutorException::class)
    fun CommandContext<Any>.requirePlayer(): OnlineKPlayer {
        val sender = commands.getSender(this)
        return when (sender) {
            is ConsoleKCommandSender -> throw NotPlayerExecutorException()
            is KPlayerKCommandSender -> sender.instance
        }
    }

    /** Returns the [KCommandSender] (player or console) who issued this command. */
    fun CommandContext<Any>.getSender(): KCommandSender {
        return commands.getSender(this)
    }
}
