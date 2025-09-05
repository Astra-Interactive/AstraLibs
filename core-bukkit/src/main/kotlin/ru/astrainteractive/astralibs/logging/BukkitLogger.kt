package ru.astrainteractive.astralibs.logging

import org.bukkit.Bukkit
import ru.astrainteractive.klibs.mikro.core.logging.JUtiltLogger
import ru.astrainteractive.klibs.mikro.core.logging.Logger

class BukkitLogger(
    override val TAG: String
) : Logger by JUtiltLogger(
    TAG = TAG,
    logger = Bukkit.getLogger()
)
