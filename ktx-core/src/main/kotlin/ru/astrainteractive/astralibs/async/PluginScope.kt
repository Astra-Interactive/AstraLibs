package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.*

/**
 * If you don't want to extend your class, just use [PluginScope.launch]
 * Don't forget to call [PluginScope.cancel] when disabling your plugin
 */
object PluginScope : AsyncTask

