package ru.astrainteractive.astralibs.economy

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import ru.astrainteractive.astralibs.economy.EconomyProvider
import ru.astrainteractive.astralibs.logging.Logger
import java.util.UUID

object VaultEconomyProvider : EconomyProvider {
    private val logger
        get() = Logger.instance
    var econ: Economy? = null
        private set
    private const val TAG = "VaultHook"
    fun offlinePlayer(uuid: UUID) = Bukkit.getOfflinePlayer(uuid)

    /**
     * @param  player player
     * @return double - current balance of [player]
     */
    fun getBalance(player: OfflinePlayer): Double? {
        return econ?.getBalance(player)
    }

    /**
     * @param  player player
     * @param  amount amount to take from balance
     * @return boolean - true if [amount] has been taken false if not
     */
    fun takeMoney(player: OfflinePlayer, amount: Double): Boolean {
        val maxBalance = getBalance(player) ?: return false
        if (amount > maxBalance)
            return false
        val response = econ?.withdrawPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    /**
     * @param  player player
     * @param  amount amount to add to balance
     * @return boolean - true if [amount] has been added false if not
     */
    fun addMoney(player: OfflinePlayer, amount: Double): Boolean {
        val response = econ?.depositPlayer(player, amount)
        return (response?.type == EconomyResponse.ResponseType.SUCCESS)
    }

    fun onEnable() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
            logger.error("Vault is not installed!", TAG)
            return
        }
        val rsp = Bukkit.getServer().servicesManager.getRegistration(Economy::class.java) ?: run {
            logger.error("Could not get economy provider", TAG)
            return
        }
        econ = rsp.provider
        logger.info("Vault hook enabled", TAG)
    }

    fun onDisable() {
        econ = null
    }

    override fun getBalance(uuid: UUID): Double? = getBalance(offlinePlayer(uuid))

    override fun takeMoney(uuid: UUID, amount: Double): Boolean = takeMoney(offlinePlayer(uuid), amount)

    override fun addMoney(uuid: UUID, amount: Double): Boolean = addMoney(offlinePlayer(uuid), amount)
}