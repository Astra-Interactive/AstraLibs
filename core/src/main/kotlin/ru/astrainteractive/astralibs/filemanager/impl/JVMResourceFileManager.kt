package ru.astrainteractive.astralibs.filemanager.impl

import ru.astrainteractive.astralibs.filemanager.ResourceFileManager
import java.io.File
import java.io.FileOutputStream
import java.net.URL
/**
 * @param name - name of the file or name with path to file
 * @param dataFolder - folder where file will be stored
 * @param clazz - class loader where resource is located
 */
class JVMResourceFileManager(
    override val name: String,
    override val dataFolder: File,
    clazz: Class<*>
) : ResourceFileManager {
    private val resource: URL? = clazz.getResource("/$name")
    override val isResourceExists: Boolean = resource != null

    override val configFile: File
        get() = loadFromResource()

    private fun loadFromResource(): File {
        val file = File(dataFolder, name)
        if (file.exists()) return file

        val resource = resource ?: throw ResourceFileManager.Exception.ResourceNotExists(name)
        val connection = resource.openConnection()?.apply {
            useCaches = false
        }
        val istream = connection?.getInputStream() ?: throw ResourceFileManager.Exception.ResourceNotExists(name)

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
        val out = FileOutputStream(file)
        val buf = ByteArray(1024)
        var len: Int
        while (istream.read(buf).also { len = it } > 0) {
            out.write(buf, 0, len)
        }
        out.close()
        istream.close()
        return file
    }

    override fun delete() {
        if (configFile.exists()) {
            configFile.delete()
        }
    }
}
