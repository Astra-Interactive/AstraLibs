package ru.astrainteractive.astralibs.economy

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

@Suppress("UnusedPrivateProperty")
class VaultEconomyProvider(
    private val economy: Economy
) : EconomyProvider {
    private fun offlinePlayer(uuid: UUID) = Bukkit.getOfflinePlayer(uuid)

    constructor(plugin: JavaPlugin) : this(
        economy = plugin.server.servicesManager.getRegistration(Economy::class.java)
            ?.provider
            ?: error("Could not get economy provider")
    )

    /**
     * @param player player
     * @return double - current balance of [player]
     */
    private fun getBalance(player: OfflinePlayer): Double {
        return economy.getBalance(player)
    }

    /**
     * @param player player
     * @param amount amount to take from balance
     * @return boolean - true if [amount] has been taken false if not
     */
    private fun takeMoney(player: OfflinePlayer, amount: Double): Boolean {
        val maxBalance = getBalance(player) ?: return false
        if (amount > maxBalance) {
            return false
        }
        val response = economy.withdrawPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    /**
     * @param player player
     * @param amount amount to add to balance
     * @return boolean - true if [amount] has been added false if not
     */
    private fun addMoney(player: OfflinePlayer, amount: Double): Boolean {
        val response = economy.depositPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    override suspend fun getBalance(uuid: UUID): Double = getBalance(offlinePlayer(uuid))

    override suspend fun takeMoney(uuid: UUID, amount: Double): Boolean = takeMoney(offlinePlayer(uuid), amount)

    override suspend fun addMoney(uuid: UUID, amount: Double): Boolean = addMoney(offlinePlayer(uuid), amount)

    override suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean = getBalance(uuid) >= amount
}
