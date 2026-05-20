package ru.astrainteractive.astralibs.economy

import java.util.UUID

/** Async facade for player economy operations. */
interface EconomyFacade {
    /** Returns the player's balance, or `null` if unavailable. */
    suspend fun getBalance(uuid: UUID): Double?

    /** Withdraws [amount] from the player's balance; returns `true` on success. */
    suspend fun takeMoney(uuid: UUID, amount: Double): Boolean

    /** Deposits [amount] into the player's balance; returns `true` on success. */
    suspend fun addMoney(uuid: UUID, amount: Double): Boolean

    /** Returns `true` if the player's balance is at least [amount]. */
    suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean
}
