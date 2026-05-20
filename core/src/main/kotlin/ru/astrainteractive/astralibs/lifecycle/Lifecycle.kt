package ru.astrainteractive.astralibs.lifecycle

/** Enable/disable/reload lifecycle contract with no-op defaults. */
interface Lifecycle {

    fun onEnable() = Unit
    fun onDisable() = Unit
    fun onReload() = Unit

    /** [Lifecycle] backed by lambdas. */
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

    /** No-op [Lifecycle] that ignores all events. */
    object Empty : Lifecycle by Lifecycle.Lambda()
}
