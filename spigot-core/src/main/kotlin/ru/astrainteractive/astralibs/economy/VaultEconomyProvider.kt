package ru.astrainteractive.astralibs.economy

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import java.util.UUID

class VaultEconomyProvider(
    private val econ: Economy = VaultEconomyServiceProvider.provide()
) : EconomyProvider {
    private fun offlinePlayer(uuid: UUID) = Bukkit.getOfflinePlayer(uuid)

    /**
     * @param player player
     * @return double - current balance of [player]
     */
    private fun getBalance(player: OfflinePlayer): Double {
        return econ.getBalance(player)
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
        val response = econ.withdrawPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    /**
     * @param player player
     * @param amount amount to add to balance
     * @return boolean - true if [amount] has been added false if not
     */
    private fun addMoney(player: OfflinePlayer, amount: Double): Boolean {
        val response = econ.depositPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    override fun getBalance(uuid: UUID): Double = getBalance(offlinePlayer(uuid))

    override fun takeMoney(uuid: UUID, amount: Double): Boolean = takeMoney(offlinePlayer(uuid), amount)

    override fun addMoney(uuid: UUID, amount: Double): Boolean = addMoney(offlinePlayer(uuid), amount)

    override fun hasAtLeast(uuid: UUID, amount: Double): Boolean = getBalance(uuid) >= amount
}
