package ru.astrainteractive.astralibs.async

import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.tooling.core.UnsafeApi

/**
 * If you don't want to extend your class, just use [PluginScope.launch]
 *
 * Don't forget to call [PluginScope.cancel] when disabling your plugin
 */
@UnsafeApi("Consider create your own PluginScope")
object PluginScope : AsyncComponent()
