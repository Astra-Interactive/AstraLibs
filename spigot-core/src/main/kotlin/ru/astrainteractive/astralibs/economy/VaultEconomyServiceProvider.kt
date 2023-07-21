package ru.astrainteractive.astralibs.economy

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import ru.astrainteractive.klibs.kdi.Provider

/**
 * This is a default provider for vault Economy
 */
internal object VaultEconomyServiceProvider : Provider<Economy> {
    override fun provide(): Economy {
        val vault = Bukkit.getPluginManager().getPlugin("Vault")
        checkNotNull(vault) { "Vault is not installed!" }
        val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)
        checkNotNull(rsp) { "Could not get economy provider" }
        return rsp.provider
    }
}
