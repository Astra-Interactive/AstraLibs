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

@Suppress("TooManyFunctions", "MaximumLineLength", "MaxLineLength")
class MultiplatformCommand(private val commands: MultiplatformCommands) {
    fun command(
        alias: String,
        block: LiteralArgumentBuilder<Any>.() -> Unit
    ): LiteralArgumentBuilder<Any> {
        val literal = commands.literal(alias)
        literal.block()
        return literal
    }

    fun LiteralArgumentBuilder<Any>.literal(
        alias: String,
        block: LiteralArgumentBuilder<Any>.() -> Unit
    ) {
        val literal = commands.literal(alias)
        literal.block()
        this.then(literal)
    }

    data class BrigadierArgument<T : Any>(
        val alias: String,
        val type: ArgumentType<T>,
        val clazz: Class<T>
    )

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

    fun RequiredArgumentBuilder<Any, *>.hints(block: (CommandContext<Any>) -> List<String>) {
        suggests { context, builder ->
            block.invoke(context).forEach(builder::suggest)
            builder.buildFuture()
        }
    }

    @Throws(IllegalArgumentException::class)
    fun <T : Any> CommandContext<Any>.requireArgument(bArgument: BrigadierArgument<T>): T {
        return getArgument(bArgument.alias, bArgument.clazz)
    }

    @Throws(ArgumentConverterException::class)
    fun <T : Any> CommandContext<Any>.requireArgument(
        bArgument: BrigadierArgument<String>,
        converter: ArgumentConverter<T>
    ): T {
        val string = getArgument(bArgument.alias, bArgument.clazz)
        return converter.transform(string)
    }

    @Throws(NoPermissionException::class)
    fun CommandContext<Any>.requirePermission(permission: Permission) {
        val sender = commands.getSender(this)
        val hasPermission = when (sender) {
            is ConsoleKCommandSender -> sender.hasPermission(permission)
            is KPlayerKCommandSender -> sender.hasPermission(permission)
        }
        if (!hasPermission) throw NoPermissionException(permission)
    }

    @Throws(NotPlayerExecutorException::class)
    fun CommandContext<Any>.requirePlayer(): OnlineKPlayer {
        val sender = commands.getSender(this)
        return when (sender) {
            is ConsoleKCommandSender -> throw NotPlayerExecutorException()
            is KPlayerKCommandSender -> sender
        }
    }

    fun CommandContext<Any>.getSender(): KCommandSender {
        return commands.getSender(this)
    }
}
