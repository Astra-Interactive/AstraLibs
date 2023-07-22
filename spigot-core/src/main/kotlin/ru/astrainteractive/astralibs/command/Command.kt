package ru.astrainteractive.astralibs.command

import org.bukkit.command.CommandSender
import ru.astrainteractive.astralibs.command.types.ArgumentType

class Command(val alias: String, val sender: CommandSender, val args: Array<out String>) {

    fun <T> argument(
        index: Int,
        argumentType: ArgumentType<T>,
    ): ArgumentResult<out T> {
        val argumentRawValue = args.getOrNull(index)
        val argumentValue = runCatching {
            argumentRawValue?.let(argumentType::transform)
        }.getOrNull()

        return argumentValue?.let { ArgumentResult.Success(it, argumentRawValue) } ?: ArgumentResult.Failure(
            argumentRawValue
        )
    }

    inline fun label(
        index: Int,
        label: String,
        block: () -> Unit
    ) {
        val argument = args.getOrNull(index)
        if (argument != label) {
            println("argument not equal label $argument $label")
            return
        }
        block.invoke()
    }
}
