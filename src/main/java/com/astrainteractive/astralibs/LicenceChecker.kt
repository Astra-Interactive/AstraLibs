package com.astrainteractive.astralibs


import com.astrainteractive.astralibs.async.AsyncHelper
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest


class LicenceChecker {
    private val licenceKey = generateLicenseKey()
    private var timer: BukkitTask? = null
    private val maxAttempts = 20
    private var currentAttempts = 0
    private fun checkLicence() = catching { URL("https://empireprojekt.ru/licence.txt").readText() }
    private fun getTimer() = Bukkit.getScheduler().runTaskTimerAsynchronously(AstraLibs.instance, Runnable {
        val text: String? = checkLicence()
        if (text == null || !text!!.contains(licenceKey)) {
            currentAttempts++
            if (currentAttempts < maxAttempts)
                return@Runnable
            AstraLibs.instance.server.broadcastMessage("#BC2551Your server doesn't have a licence from AstraInteractive".HEX())
            AstraLibs.instance.server.broadcastMessage("#BC2551This plugin is for demonstration purposes only!".HEX())
            AstraLibs.instance.server.broadcastMessage("#BC2551For more information visit links below:".HEX())
            AstraLibs.instance.server.broadcastMessage("#D28429https://github.com/Asrta-Interactive".HEX())
            AstraLibs.instance.server.broadcastMessage("#D34829https://astrainteractive.ru/".HEX())
        } else
            currentAttempts = 0
    }, 0L, 100L)

    fun onDisable() {
        timer?.cancel()
    }

    fun enable() {
        timer = getTimer()
        Logger.log("Licence key", licenceKey)
        AsyncHelper.launch {
            val licences = checkLicence()
            if (licences != null && licences.contains(licenceKey)) {
                Logger.log("Licence key", "You have a licence for ${AstraLibs.instance.name}")
            }
        }
    }

    companion object {
        fun generateLicenseKey(): String {
            val value = System.getenv().toString().toByteArray()
            val digest = MessageDigest.getInstance("SHA-1")
            digest.reset()
            digest.update(value)
            return String.format("%040x", BigInteger(1, digest.digest()))
        }
    }
}