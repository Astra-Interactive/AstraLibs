package ru.astrainteractive.astralibs.economy

import java.util.UUID

interface EconomyProvider {
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

    /**
     * @param uuid - UUID of player
     * @param amount - amount of requested money
     * @return boolean - true if player has at least [amount] money, false if not
     */
    fun hasAtLeast(uuid: UUID, amount: Double): Boolean
}