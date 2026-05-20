package ru.astrainteractive.astralibs.economy

import java.util.UUID

/** No-op [EconomyFacade] that always returns `null`/`false`. Useful as a stub or test double. */
class EmptyEconomyFacade : EconomyFacade {

    override suspend fun getBalance(uuid: UUID): Double? = null

    override suspend fun takeMoney(uuid: UUID, amount: Double): Boolean = false

    override suspend fun addMoney(uuid: UUID, amount: Double): Boolean = false

    override suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean = false
}
