package ru.astrainteractive.astralibs.command.api

import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

fun interface Command<R : Any, I : Any> {
    /**
     * Register Bukkit command
     */
    fun register(javaPlugin: JavaPlugin)

    /**
     * Result handler will handle [CommandParser] output
     *
     * It can send a message to executor if parsing was not successful
     */
    fun interface ResultHandler<R : Any> {
        /**
         * Handle your parsed result. Send message etc
         */
        fun handle(commandSender: CommandSender, result: R)

        class NoOp<R : Any> : ResultHandler<R> {
            override fun handle(commandSender: CommandSender, result: R) = Unit
        }
    }

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
