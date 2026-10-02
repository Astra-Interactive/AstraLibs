package ru.astrainteractive.astralibs.server.permission

import net.luckperms.api.LuckPerms

/** Gives the [LuckPerms] API to code that must keep working while LuckPerms is absent. */
interface LuckPermsProvider {
    /** Returns a failure while LuckPerms is not installed or not enabled yet. */
    fun provide(): Result<LuckPerms>

    /** [LuckPermsProvider] backed by the LuckPerms singleton, which LuckPerms registers on every platform. */
    object Default : LuckPermsProvider {
        override fun provide(): Result<LuckPerms> {
            return runCatching { net.luckperms.api.LuckPermsProvider.get() }
        }
    }
}
