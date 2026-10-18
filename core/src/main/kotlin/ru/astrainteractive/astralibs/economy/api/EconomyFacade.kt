package ru.astrainteractive.astralibs.economy.api

import java.util.UUID

/** Async facade for player economy operations. */
public interface EconomyFacade {
    /** Returns the player's balance, or `null` if unavailable. */
    public suspend fun getBalance(uuid: UUID): Double?

    /** Withdraws [amount] from the player's balance; returns `true` on success. */
    public suspend fun takeMoney(uuid: UUID, amount: Double): Boolean

    /** Deposits [amount] into the player's balance; returns `true` on success. */
    public suspend fun addMoney(uuid: UUID, amount: Double): Boolean

    /** Returns `true` if the player's balance is at least [amount]. */
    public suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean
}
