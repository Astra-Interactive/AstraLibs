package com.astrainteractive.astralibs

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player


class AstraCommandBuilder(
    private var defaultLabel: String? = null,
    private var label: String? = null,
    private var defaultArgsSize: Int = -1,
    private var argsSize: Int = -1,
    private var sender: CommandSender? = null,
    private var permission: Array<out String>? = arrayOf(),
    private var example: String = "",
    private var explain: String = "",
    private var playerCommand: () -> Unit? = {},
    private var consoleCommand: () -> Unit? = {}
) {
    /**
     * Default label for command.
     *
     * Example: /open gui 1 - here open is a label
     * And Label, which player entered
     */
    fun label(defaultLabel: String, label: String) = apply {
        this.label = label
        this.defaultLabel = defaultLabel
    }

    /**
     * Size of args, which player entered
     *
     * And Default argument size of command
     */
    fun argsSize(defaultArgsSize: Int, argsSize: Int) = apply {
        this.defaultArgsSize = defaultArgsSize
        this.argsSize = argsSize
    }

    /**
     * Sender of command
     */
    fun sender(sender: CommandSender) = apply { this.sender = sender }

    /**
     * Example usage of command
     */
    fun example(example: String) = apply { this.example = example }

    /**
     * Explanation of command
     */
    fun explain(explain: String) = apply { this.explain = explain }

    /**
     * Permission for current command
     */
    fun permission(vararg permission: String) = apply { this.permission = permission }

    /**
     * Command which executed when player execute it
     */
    fun playerCommand(function: () -> Unit) = apply { this.playerCommand = function }

    /**
     * Command which executed when console execute it
     */
    fun consoleCommand(function: () -> Unit) = apply { this.consoleCommand = function }

    /**
     * Command builder
     */
    fun build(): Response {
        if (defaultLabel != this.defaultLabel)
            return Response.WRONG_LABEL
        if (argsSize ?: 0 != this.defaultArgsSize)
            return Response.WRONG_ARGS_SIZE
        permission?.let {
            for (perm in it.iterator())
                if (sender?.hasPermission(perm) != true)
                    return Response.NO_PERMISSION
        }
        if (sender == null)
            return Response.NO_SENDER
        if (sender is ConsoleCommandSender)
            consoleCommand.invoke()
        if (sender is Player)
            playerCommand.invoke()
        return Response.SUCCESS
    }
    enum class Response {
        WRONG_LABEL,
        WRONG_ARGS_SIZE,
        NO_PERMISSION,
        NO_SENDER,
        SUCCESS
    }
}