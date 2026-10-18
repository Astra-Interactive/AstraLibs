package ru.astrainteractive.astralibs.command.brigadier.model

import com.mojang.brigadier.arguments.ArgumentType

/** Metadata needed to register and extract a typed Brigadier argument. */
public data class BrigadierArgument<T : Any>(
    val alias: String,
    val type: ArgumentType<T>,
    val clazz: Class<T>
)
