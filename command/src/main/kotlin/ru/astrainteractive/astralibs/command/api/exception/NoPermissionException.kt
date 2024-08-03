package ru.astrainteractive.astralibs.command.api.exception

import ru.astrainteractive.astralibs.permission.Permission

class NoPermissionException(val permission: Permission) : CommandException("No permission: $permission")
