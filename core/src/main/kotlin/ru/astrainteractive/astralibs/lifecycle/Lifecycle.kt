package ru.astrainteractive.astralibs.lifecycle

interface Lifecycle {

    fun onEnable() = Unit

    fun onDisable() = Unit

    fun onReload() = Unit

    class Lambda(
        private val onEnable: Lifecycle.() -> Unit = {},
        private val onDisable: Lifecycle.() -> Unit = {},
        private val onReload: Lifecycle.() -> Unit = {},
    ) : Lifecycle {
        override fun onEnable() {
            onEnable.invoke(this)
        }

        override fun onDisable() {
            onDisable.invoke(this)
        }

        override fun onReload() {
            onReload.invoke(this)
        }
    }
}
