package ru.astrainteractive.astralibs.economy

import java.util.UUID

/**
 * EmptyEconomyProvider can be used when no other economy provider found
 *
 * It will return failure to every economy operation
 */
class EmptyEconomyProvider : EconomyProvider {
    override fun getBalance(uuid: UUID): Double? = null

    override fun takeMoney(uuid: UUID, amount: Double): Boolean = false

    override fun addMoney(uuid: UUID, amount: Double): Boolean = false

    override fun hasAtLeast(uuid: UUID, amount: Double): Boolean = false
}
