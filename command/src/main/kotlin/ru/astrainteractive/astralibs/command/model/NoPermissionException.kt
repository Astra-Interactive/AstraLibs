package ru.astrainteractive.astralibs.command.model

import ru.astrainteractive.astralibs.server.permission.api.Permission

/** Thrown when the command executor does not hold a required [Permission]. */
public class NoPermissionException(
    permission: Permission
) : CommandException("No permission: $permission")
