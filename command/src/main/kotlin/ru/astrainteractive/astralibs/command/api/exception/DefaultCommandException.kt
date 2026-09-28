package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.localization.component.LocalizableComponent
import ru.astrainteractive.astralibs.server.permission.Permission

/**
 * Thrown when a command execution fails with a message that is shown in the sender's language. The exception's
 * own message stays generic: the reason is only readable once [localizableComponent] is rendered for someone.
 */
class LocalizableComponentCommandException(
    val localizableComponent: LocalizableComponent
) : CommandException("Command failed with a localizable message for its sender")

/** Thrown when a command argument value is incompatible with the expected [ArgumentConverter] type. */
class BadArgumentException(
    val wrongArgument: String?,
    val type: ArgumentConverter<*>
) : CommandException("Incompatible type $type for argument $wrongArgument")

/** Thrown by an [ArgumentConverter] when it cannot parse [value] into its target type. */
class ArgumentConverterException(
    val clazz: Class<out ArgumentConverter<*>>,
    val value: String
) : CommandException("Argument type $clazz could not parse $value")

/** Thrown when the command executor does not hold a required [Permission]. */
class NoPermissionException(
    val permission: Permission
) : CommandException("No permission: $permission")

/** Thrown when a player lookup yields no result. */
class NoPlayerException(
    val name: String
) : CommandException("Player $name not found")

/** Thrown when a command that requires a player executor is run by a non-player sender. */
class NotPlayerExecutorException : CommandException("Executor should be player")

/** Thrown when a potion effect type lookup yields no result. */
class NoPotionEffectTypeException(
    val name: String
) : CommandException("PotionEffectType $name not found")
