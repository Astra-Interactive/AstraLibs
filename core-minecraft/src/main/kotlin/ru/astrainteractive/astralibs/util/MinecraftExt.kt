package ru.astrainteractive.astralibs.util

import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack

/** Exposes the private `source` field of [CommandSourceStack] for sender-type inspection. */
internal val CommandSourceStack.publicSource: CommandSource
    get() = this.privateField(
        fieldName = this::class.java.declaredFields
            .firstOrNull { field -> field.type::class.java == CommandSourceStack::class.java }
            ?.name
            ?: error("Could not get name of the field")
    )
