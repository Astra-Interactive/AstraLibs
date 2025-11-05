package ru.astrainteractive.astralibs.lifecycle

/**
 * A general-purpose lifecycle interface representing typical lifecycle stages:
 * enable, disable, and reload.
 *
 * Implementers can override any of the lifecycle methods to add behavior.
 */
interface Lifecycle {

    /**
     * Called when the component is enabled.
     * Default implementation does nothing.
     */
    fun onEnable() = Unit

    /**
     * Called when the component is disabled.
     * Default implementation does nothing.
     */
    fun onDisable() = Unit

    /**
     * Called when the component is reloaded.
     * Default implementation does nothing.
     */
    fun onReload() = Unit

    /**
     * A [Lifecycle] implementation that allows passing lambda functions for each lifecycle event.
     *
     * Useful for lightweight or dynamic lifecycle definitions.
     *
     * @param onEnable Lambda executed on `onEnable()`.
     * @param onDisable Lambda executed on `onDisable()`.
     * @param onReload Lambda executed on `onReload()`.
     */
    class Lambda(
        private val onEnable: Lifecycle.() -> Unit = {},
        private val onDisable: Lifecycle.() -> Unit = {},
        private val onReload: Lifecycle.() -> Unit = {},
    ) : Lifecycle {
        /**
         * Invokes the lambda passed to the constructor for the enable phase.
         */
        override fun onEnable() {
            onEnable.invoke(this)
        }

        /**
         * Invokes the lambda passed to the constructor for the disable phase.
         */
        override fun onDisable() {
            onDisable.invoke(this)
        }

        /**
         * Invokes the lambda passed to the constructor for the reload phase.
         */
        override fun onReload() {
            onReload.invoke(this)
        }
    }

    object Empty : Lifecycle by Lifecycle.Lambda()
}
