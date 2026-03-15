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
class MultiplatformCommand<CommandSourceStack>(
    val commands: MultiplatformCommands<CommandSourceStack>
) {
    fun command(
        alias: String,
        block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
    ): LiteralArgumentBuilder<CommandSourceStack> {
        val literal = commands.literal(alias)
        literal.block()
        return literal
    }

    fun LiteralArgumentBuilder<CommandSourceStack>.literal(
        alias: String,
        block: LiteralArgumentBuilder<CommandSourceStack>.() -> Unit
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
        val argument = commands.argument(alias, type)
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
        val argument = commands.argument(alias, type)
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

    @Throws(IllegalArgumentException::class)
    fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(bArgument: BrigadierArgument<T>): T {
        return getArgument(bArgument.alias, bArgument.clazz)
    }

    @Throws(ArgumentConverterException::class)
    fun <T : Any> CommandContext<CommandSourceStack>.requireArgument(
        bArgument: BrigadierArgument<String>,
        converter: ArgumentConverter<T>
    ): T {
        val string = getArgument(bArgument.alias, bArgument.clazz)
        return converter.transform(string)
    }

    @Throws(NoPermissionException::class)
    fun CommandContext<CommandSourceStack>.requirePermission(permission: Permission) {
        val sender = commands.getSender(this)
        val hasPermission = when (sender) {
            is ConsoleKCommandSender -> sender.hasPermission(permission)
            is KPlayerKCommandSender -> sender.hasPermission(permission)
        }
        if (!hasPermission) throw NoPermissionException(permission)
    }

    @Throws(NotPlayerExecutorException::class)
    fun CommandContext<CommandSourceStack>.requirePlayer(): OnlineKPlayer {
        val sender = commands.getSender(this)
        return when (sender) {
            is ConsoleKCommandSender -> throw NotPlayerExecutorException()
            is KPlayerKCommandSender -> sender
        }
    }

    @Throws(NotPlayerExecutorException::class)
    fun CommandContext<CommandSourceStack>.platformSender(): KCommandSender {
        return commands.getSender(this)
    }

    fun CommandContext<CommandSourceStack>.getSender(): KCommandSender {
        return commands.getSender(this)
    }
}
