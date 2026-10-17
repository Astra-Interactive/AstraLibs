package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.command.api.argumenttype.ArgumentConverter
import ru.astrainteractive.astralibs.localization.component.LocalizableComponent
import ru.astrainteractive.astralibs.server.permission.Permission

/**
 * Thrown when a command execution fails with a message that is shown in the sender's language. The exception's
 * own message stays generic: the reason is only readable once [localizableComponent] is rendered for someone.
 */
public class LocalizableComponentCommandException(
    public val localizableComponent: LocalizableComponent
) : CommandException("Command failed with a localizable message for its sender")

/** Thrown when a command argument value is incompatible with the expected [ArgumentConverter] type. */
public class BadArgumentException(
    wrongArgument: String?,
    type: ArgumentConverter<*>
) : CommandException("Incompatible type $type for argument $wrongArgument")

/** Thrown by an [ArgumentConverter] when it cannot parse [value] into its target type. */
public class ArgumentConverterException(
    clazz: Class<out ArgumentConverter<*>>,
    value: String
) : CommandException("Argument type $clazz could not parse $value")

/** Thrown when the command executor does not hold a required [Permission]. */
public class NoPermissionException(
    permission: Permission
) : CommandException("No permission: $permission")

/** Thrown when a player lookup yields no result. */
public class NoPlayerException(
    name: String
) : CommandException("Player $name not found")

/** Thrown when a command that requires a player executor is run by a non-player sender. */
public class NotPlayerExecutorException : CommandException("Executor should be player")

/** Thrown when a potion effect type lookup yields no result. */
public class NoPotionEffectTypeException(
    name: String
) : CommandException("PotionEffectType $name not found")
