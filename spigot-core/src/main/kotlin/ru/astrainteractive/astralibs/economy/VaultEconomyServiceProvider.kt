package ru.astrainteractive.astralibs.economy

import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import ru.astrainteractive.astralibs.Provider

/**
 * This is a default provider for vault Economy
 */
internal object VaultEconomyServiceProvider : Provider<Economy> {
    override fun provide(): Economy {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            throw Exception("Vault is not installed!")
        }
        val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)
            ?: throw Exception("Could not get economy provider")
        return rsp.provider
    }
}