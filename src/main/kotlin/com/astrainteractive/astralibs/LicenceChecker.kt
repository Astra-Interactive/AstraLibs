package com.astrainteractive.astralibs

import org.apache.commons.codec.digest.DigestUtils
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitTask
import oshi.SystemInfo
import java.net.URL

class LicenceChecker() {
    private val licenceKey = generateLicenseKey()
    private var timer: BukkitTask?=null
    private fun checkLicence() = catching{ URL("https://empireprojekt.ru/licence.txt").readText() }
    private fun getTimer() = Bukkit.getScheduler().runTaskTimerAsynchronously(AstraLibs.instance, Runnable {
        val text:String? = checkLicence()
        if (text==null || !text!!.contains(licenceKey)) {
            AstraLibs.instance.server.broadcastMessage("#BC2551Your server doesn't have a licence from AstraInteractive".HEX())
            AstraLibs.instance.server.broadcastMessage("#BC2551This plugin is for demonstration purposes only!".HEX())
            AstraLibs.instance.server.broadcastMessage("#BC2551For more information visit links below:".HEX())
            AstraLibs.instance.server.broadcastMessage("#D28429https://github.com/Asrta-Interactive".HEX())
            AstraLibs.instance.server.broadcastMessage("#D34829https://astrainteractive.ru/".HEX())
        }
    }, 0L, 100L)
    fun onDisable(){
        timer?.cancel()
    }
    fun enable(){
        timer = getTimer()
        Logger.log("Licence key",licenceKey)
        runAsyncTask{
            val licences = checkLicence()
            if (licences!=null && licences.contains(licenceKey)){
                Logger.log("Licence key","You have a licence for ${AstraLibs.instance.name}")
            }
        }
    }

    companion object {
        fun generateLicenseKey(): String {
            val systemInfo = SystemInfo()
            val operatingSystem = systemInfo.operatingSystem
            val hardwareAbstractionLayer = systemInfo.hardware
            val centralProcessor = hardwareAbstractionLayer.processor
            val computerSystem = hardwareAbstractionLayer.computerSystem
            val vendor = operatingSystem.manufacturer
            val processorSerialNumber = computerSystem.serialNumber
            val processorIdentifier: String = centralProcessor.processorIdentifier.identifier
            val processors = centralProcessor.logicalProcessorCount
            val delimiter = "#"
            val key = vendor +
                    delimiter +
                    processorSerialNumber +
                    delimiter +
                    processorIdentifier +
                    delimiter +
                    processors
            return DigestUtils.sha1Hex(key)
        }
    }
}