package ru.astrainteractive.astralibs.economy

import java.util.UUID

class EmptyEconomyProvider : EconomyProvider {
    override fun getBalance(uuid: UUID): Double? = null

    override fun takeMoney(uuid: UUID, amount: Double): Boolean = false

    override fun addMoney(uuid: UUID, amount: Double): Boolean = false

    override fun hasAtLeast(uuid: UUID, amount: Double): Boolean = false
}
