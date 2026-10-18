package ru.astrainteractive.astralibs.command.model

import ru.astrainteractive.astralibs.localization.component.LocalizableComponent

/**
 * Thrown when a command execution fails with a message that is shown in the sender's language. The exception's
 * own message stays generic: the reason is only readable once [localizableComponent] is rendered for someone.
 */
public class LocalizableComponentCommandException(
    public val localizableComponent: LocalizableComponent
) : CommandException("Command failed with a localizable message for its sender")
