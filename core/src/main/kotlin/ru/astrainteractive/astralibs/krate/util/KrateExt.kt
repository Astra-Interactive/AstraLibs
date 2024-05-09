package ru.astrainteractive.astralibs.krate.util

import ru.astrainteractive.astralibs.krate.core.FileKrate
import java.io.File

object KrateExt {
    /**
     * Delete entire storage file
     */
    fun FileKrate<*>.delete() {
        File(folder, fileName).delete()
    }

    /**
     * Delete entire storage
     */
    fun FileKrate<*>.deleteAll() {
        folder.delete()
    }
}
