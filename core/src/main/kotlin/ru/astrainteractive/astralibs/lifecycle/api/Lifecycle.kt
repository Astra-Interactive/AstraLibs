package ru.astrainteractive.astralibs.lifecycle.api

/** Enable/disable/reload lifecycle contract with no-op defaults. */
public interface Lifecycle {

    public fun onEnable(): Unit = Unit
    public fun onDisable(): Unit = Unit
    public fun onReload(): Unit = Unit

    /** [Lifecycle] backed by lambdas. */
    public class Lambda(
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
    public object Empty : Lifecycle by Lifecycle.Lambda()
}
