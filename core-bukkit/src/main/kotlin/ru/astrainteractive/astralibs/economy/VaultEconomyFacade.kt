package ru.astrainteractive.astralibs.economy

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

/** [EconomyFacade] backed by the Vault economy API. */
@Suppress("UnusedPrivateProperty")
class VaultEconomyFacade(
    private val economy: Economy
) : EconomyFacade {

    /** @throws IllegalStateException if no economy provider is registered. */
    constructor(plugin: JavaPlugin) : this(
        economy = plugin.server.servicesManager.getRegistration(Economy::class.java)
            ?.provider
            ?: error("Could not get economy provider")
    )

    private fun offlinePlayer(uuid: UUID) = Bukkit.getOfflinePlayer(uuid)

    private fun getBalance(player: OfflinePlayer): Double {
        return economy.getBalance(player)
    }

    private fun takeMoney(player: OfflinePlayer, amount: Double): Boolean {
        val maxBalance = getBalance(player) ?: return false
        if (amount > maxBalance) {
            return false
        }
        val response = economy.withdrawPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    private fun addMoney(player: OfflinePlayer, amount: Double): Boolean {
        val response = economy.depositPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    override suspend fun getBalance(uuid: UUID): Double = getBalance(offlinePlayer(uuid))

    override suspend fun takeMoney(uuid: UUID, amount: Double): Boolean = takeMoney(offlinePlayer(uuid), amount)

    override suspend fun addMoney(uuid: UUID, amount: Double): Boolean = addMoney(offlinePlayer(uuid), amount)

    override suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean = getBalance(uuid) >= amount
}
