package ru.astrainteractive.astralibs.economy

import java.util.UUID

/**
 * A no-op implementation of [EconomyFacade].
 *
 * This implementation always returns default values, indicating that
 * no actual economy operations are performed. It can be used as a fallback,
 * placeholder, or for testing purposes where economy functionality is not required.
 */
class EmptyEconomyFacade : EconomyFacade {

    /**
     * Always returns `null`, indicating no balance is available.
     *
     * @param uuid The UUID of the player.
     * @return Always `null`.
     */
    override suspend fun getBalance(uuid: UUID): Double? = null

    /**
     * Always returns `false`, indicating money was not taken.
     *
     * @param uuid The UUID of the player.
     * @param amount The amount to withdraw.
     * @return Always `false`.
     */
    override suspend fun takeMoney(uuid: UUID, amount: Double): Boolean = false

    /**
     * Always returns `false`, indicating money was not added.
     *
     * @param uuid The UUID of the player.
     * @param amount The amount to add.
     * @return Always `false`.
     */
    override suspend fun addMoney(uuid: UUID, amount: Double): Boolean = false

    /**
     * Always returns `false`, indicating the player does not have the required amount.
     *
     * @param uuid The UUID of the player.
     * @param amount The minimum amount to check.
     * @return Always `false`.
     */
    override suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean = false
}
