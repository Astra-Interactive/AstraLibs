package ru.astrainteractive.astralibs.economy

import java.util.UUID

/**
 * Interface representing a facade for economy-related operations.
 * Provides asynchronous methods to interact with player balances.
 */
interface EconomyFacade {
    /**
     * Retrieves the balance of the player associated with the given [uuid].
     *
     * @param uuid The UUID of the player.
     * @return The player's balance, or `null` if unavailable.
     */
    suspend fun getBalance(uuid: UUID): Double?

    /**
     * Attempts to withdraw a specific [amount] of money from the player's balance.
     *
     * @param uuid The UUID of the player.
     * @param amount The amount to withdraw.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun takeMoney(uuid: UUID, amount: Double): Boolean

    /**
     * Adds a specific [amount] of money to the player's balance.
     *
     * @param uuid The UUID of the player.
     * @param amount The amount to deposit.
     * @return `true` if the operation was successful, `false` otherwise.
     */
    suspend fun addMoney(uuid: UUID, amount: Double): Boolean

    /**
     * Checks whether the player has at least a certain [amount] of money.
     *
     * @param uuid The UUID of the player.
     * @param amount The minimum amount to check.
     * @return `true` if the player has at least the specified amount, `false` otherwise.
     */
    suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean
}
