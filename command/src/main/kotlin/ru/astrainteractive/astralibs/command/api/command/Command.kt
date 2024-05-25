package ru.astrainteractive.astralibs.command.api.command

import ru.astrainteractive.astralibs.command.api.context.CommandContext

interface Command<CC : CommandContext> {
    val alias: String

    /**
     * Dispatch command
     */
    fun dispatch(commandContext: CC)

    /**
     * This mapper is used to map Result from [CommandParser] to Input of [CommandExecutor]
     */
    fun interface Mapper<R : Any, I : Any> {
        /**
         * Transform result of type [R] into input [I]
         *
         * Since we can't guarantee the safety of [I] it's nullable
         */
        fun toInput(result: R): I?

        /**
         * Use [NoOp] if you don't have input [I] type
         */
        class NoOp<R : Any> : Mapper<R, R> {
            override fun toInput(result: R): R = result
        }
    }
}
