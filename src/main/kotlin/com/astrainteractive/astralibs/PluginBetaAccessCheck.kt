package com.astrainteractive.astralibs

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.text.SimpleDateFormat
import java.util.*

class PluginBetaAccessCheck() {
    init {
        initBetaPluginChecker()
    }

    private fun initBetaPluginChecker() {
        Bukkit.getScheduler().runTaskTimer(AstraLibs.instance, Runnable { checkTime() }, 0, 500)
    }

    private fun getDate(milliSeconds: Long, dateFormat: String?): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    private fun checkTime(): Boolean {
        fun hoursToMS(h: Int): Long {
            return h * 60 * 60 * 1000L
        }

        fun minuteToMS(m: Int): Long {
            return m * 60 * 1000L
        }

        val maxTime: Long = hoursToMS(7000)// * 60 * 1000
        val time: Long = 1626024523239
        val date = getDate(time + maxTime, "dd/MM/yyyy HH:mm:ss")
        AstraLibs.instance.server.broadcastMessage("${ChatColor.RED}Используется тестовая версия плагина EmpireItems.")
        AstraLibs.instance.server.broadcastMessage("${ChatColor.RED}Плагин будет отключен $date")
        AstraLibs.instance.server.broadcastMessage("${ChatColor.RED}EmpireProjekt.ru")

        if (System.currentTimeMillis() - time < maxTime)
            println(
                "${ChatColor.RED}" +
                        "Вы используете тестовую версию! Плагин отключится " +
                        date
            )
        else {
            println("${ChatColor.RED}Тестовая версия плагина закончилась! Зайтите в дискорд EmpireProjekt.ru/discord и попросите новую версию")
            AstraLibs.instance.server.broadcastMessage("${ChatColor.RED}Тестовая версия плагина закончилась! Зайтите в дискорд EmpireProjekt.ru/discord и попросите новую версию")
            AstraLibs.instance.onDisable()
            return false
        }
        return true

    }
}