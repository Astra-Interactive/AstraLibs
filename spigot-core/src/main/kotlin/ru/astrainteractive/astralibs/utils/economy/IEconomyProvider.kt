package ru.astrainteractive.astralibs.utils.economy

import org.bukkit.OfflinePlayer
import java.util.UUID

interface IEconomyProvider {
    /**
     * @param  uuid UUID of player
     * @return double - current balance of [uuid]
     */
    fun getBalance(uuid: UUID): Double?

    /**
     * @param  uuid UUID of player
     * @param  amount amount to take from balance
     * @return boolean - true if [amount] has been taken false if not
     */
    fun takeMoney(uuid: UUID, amount: Double): Boolean

    /**
     * @param  uuid player
     * @param  amount amount to add to balance
     * @return boolean - true if [amount] has been added false if not
     */
    fun addMoney(uuid: UUID, amount: Double): Boolean
}