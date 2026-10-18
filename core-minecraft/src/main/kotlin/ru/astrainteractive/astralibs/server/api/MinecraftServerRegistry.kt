package ru.astrainteractive.astralibs.server.api

import net.minecraft.server.MinecraftServer
import java.lang.ref.WeakReference
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Thread-safe weak-reference registry for the running [MinecraftServer].
 *
 * Call [setServer] with the live instance on startup and `null` on shutdown.
 */
public object MinecraftServerRegistry {
    private val lock = ReentrantLock()
    private var _server: WeakReference<MinecraftServer>? = null

    /** Returns the registered server, or `null` if unset or GC'd. */
    public val serverOrNull: MinecraftServer?
        get() = lock.withLock { _server?.get() }

    /** Registers or clears the current [MinecraftServer] reference. */
    public fun setServer(server: MinecraftServer?) {
        lock.withLock { _server = WeakReference(server) }
    }

    /** Returns the registered server, or throws [IllegalStateException] if unavailable. */
    public fun requireServer(): MinecraftServer {
        return lock
            .withLock { _server?.get() }
            ?: error("Server is not available")
    }
}
