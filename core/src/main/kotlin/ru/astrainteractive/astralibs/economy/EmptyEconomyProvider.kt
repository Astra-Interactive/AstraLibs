package ru.astrainteractive.astralibs.economy

import java.util.UUID

/**
 * EmptyEconomyProvider can be used when no other economy provider found
 *
 * It will return failure to every economy operation
 */
class EmptyEconomyProvider : EconomyProvider {
    override suspend fun getBalance(uuid: UUID): Double? = null

    override suspend fun takeMoney(uuid: UUID, amount: Double): Boolean = false

    override suspend fun addMoney(uuid: UUID, amount: Double): Boolean = false

    override suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean = false
}
