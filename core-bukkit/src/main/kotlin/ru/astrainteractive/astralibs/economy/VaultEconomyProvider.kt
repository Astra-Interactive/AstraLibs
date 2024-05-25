package ru.astrainteractive.astralibs.economy

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import ru.astrainteractive.klibs.kdi.Factory
import java.util.UUID

class VaultEconomyProvider(plugin: JavaPlugin, vault: Plugin) : EconomyProvider {
    private val economy = Factory {
        val rsp = plugin.server.servicesManager.getRegistration(Economy::class.java)
        checkNotNull(rsp) { "Could not get economy provider" }
        rsp.provider
    }.create()

    private fun offlinePlayer(uuid: UUID) = Bukkit.getOfflinePlayer(uuid)

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

    override fun getBalance(uuid: UUID): Double = getBalance(offlinePlayer(uuid))

    override fun takeMoney(uuid: UUID, amount: Double): Boolean = takeMoney(offlinePlayer(uuid), amount)

    override fun addMoney(uuid: UUID, amount: Double): Boolean = addMoney(offlinePlayer(uuid), amount)

    override fun hasAtLeast(uuid: UUID, amount: Double): Boolean = getBalance(uuid) >= amount
}
