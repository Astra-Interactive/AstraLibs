package ru.astrainteractive.astralibs.logging

import org.bukkit.Bukkit

class BukkitLogger(
    override val TAG: String
) : Logger by JUtiltLogger(
    TAG = TAG,
    logger = Bukkit.getLogger()
)
