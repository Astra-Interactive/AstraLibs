package ru.astrainteractive.astralibs.economy

import com.earth2me.essentials.api.Economy
import java.math.BigDecimal
import java.util.UUID

object EssentialsEconomyFacade : EconomyFacade {

    override suspend fun addMoney(uuid: UUID, amount: Double): Boolean = runCatching {
        Economy.add(uuid, BigDecimal(amount))
    }.map { true }.onFailure(Throwable::printStackTrace).getOrNull() ?: false

    override suspend fun getBalance(uuid: UUID): Double? = runCatching {
        Economy.getMoneyExact(uuid).toDouble()
    }.onFailure(Throwable::printStackTrace).getOrNull()

    override suspend fun hasAtLeast(uuid: UUID, amount: Double): Boolean = runCatching {
        Economy.hasMore(uuid, BigDecimal(amount))
    }.onFailure(Throwable::printStackTrace).getOrNull() ?: false

    override suspend fun takeMoney(uuid: UUID, amount: Double): Boolean {
        if (!hasAtLeast(uuid, amount)) return false
        return runCatching { Economy.subtract(uuid, BigDecimal(amount)) }
            .map { true }
            .onFailure(Throwable::printStackTrace)
            .getOrNull() ?: false
    }
}
