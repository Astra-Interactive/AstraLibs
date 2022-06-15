package com.astrainteractive.astralibs

import org.bukkit.Bukkit

enum class ServerVersion {
    v1_13_R1,
    v1_13_1_R1,
    v1_13_2_R1,
    v1_14_R1,
    v1_14_1_R1,
    v1_14_2_R1,
    v1_14_3_R1,
    v1_14_4_R1,
    v1_15_R1,
    v1_15_1_R1,
    v1_15_2_R1,
    v1_16_1_R1,
    v1_16_2_R1,
    v1_16_3_R1,
    v1_16_4_R1,
    v1_16_5_R1,
    v1_17_R1,
    v1_17_1_R1,
    v1_18_1_R1,
    v1_18_R1,
    v1_19_R1,
    UNMAINTAINED;

    override fun toString(): String = name

    companion object {
        var version: ServerVersion = UNMAINTAINED
            private set

        fun getServerVersion(): ServerVersion {
            val v = Bukkit.getServer().javaClass.packageName.split(".").last()
            version = valueOfOrNull<ServerVersion>(v) ?: UNMAINTAINED
            return version
        }
    }
}


