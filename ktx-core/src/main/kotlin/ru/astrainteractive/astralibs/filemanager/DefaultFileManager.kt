package ru.astrainteractive.astralibs.filemanager

import java.io.File
import java.io.FileOutputStream
import java.net.URL

class DefaultFileManager(
    clazz: Class<*>,
    override val name: String,
    override val dataFolder: File
) : FileManager {
    private val resource: URL? = clazz.getResource("/$name")
    override val isResourceExists: Boolean = resource != null

    override var configFile: File = loadConfigFile()
        private set


    override fun loadFromResource(): File {
        val resource = resource ?: throw FileManager.Exception.ResourceNotExists(name)
        val connection = resource.openConnection()
        connection?.useCaches = false
        val istream = connection?.getInputStream() ?: throw FileManager.Exception.ResourceNotExists(name)
        val file = File(dataFolder, name)

        if (file.exists()) return file
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

    override fun loadConfigFile(): File {
        val file = File(dataFolder, name)
        if (file.exists()) return file
        if (isResourceExists)
            return loadFromResource()

        File(dataFolder, name)
        file.parentFile?.mkdirs()
        file.createNewFile()
        return file
    }

    override fun save() {
        throw Exception("DefaultFileManager has no need to use this method")
    }

    override fun reload() {
        this.configFile = loadConfigFile()
    }
}