package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.permission.Permission

class NoPermissionException(permission: Permission) : CommandException("No permission: $permission")
