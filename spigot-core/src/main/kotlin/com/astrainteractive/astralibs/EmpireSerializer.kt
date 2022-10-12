package com.astrainteractive.astralibs

import com.astrainteractive.astralibs.file_manager.FileManager
import com.charleskorn.kaml.DuplicateKeyException
import com.charleskorn.kaml.EmptyYamlDocumentException
import com.charleskorn.kaml.Yaml
import kotlinx.serialization.serializer
import java.io.File


object EmpireSerializer {
    const val TAG = "EmpireSerializer"
    inline fun <reified T> toClass(file: FileManager): T? = toClass(file.configFile)
    inline fun <reified T> toClass(file: File): T? = try {
        val yaml = Yaml(
            Yaml.default.serializersModule,
            configuration = Yaml.default.configuration.copy(strictMode = true)
        )
        yaml.decodeFromString(serializer(T::class.java), file.readText()) as? T
    } catch (e: EmptyYamlDocumentException) {
        Logger.error("${e.message} ${file.name}", TAG)
        null
    } catch (e: DuplicateKeyException) {
        Logger.error(e.message, TAG)
        null
    } catch (e: kotlinx.serialization.SerializationException) {
        Logger.error(
            "Error serializing file ${file.name} with class ${T::class.simpleName}: ${e.message ?: e.localizedMessage}",
            TAG
        )
        null
    } catch (e: NoSuchMethodError) {
        Logger.error(
            "Error serializing file ${file.name} with class ${T::class.simpleName}: ${e.message ?: e.localizedMessage}",
            TAG
        )
        null
    } catch (e: Exception) {
        Logger.error(
            "Error serializing file ${file.name} with class ${T::class.simpleName}: ${e.message ?: e.localizedMessage}",
            TAG
        )
        null
    } catch (e: java.lang.Exception) {
        Logger.error(
            "Error serializing file ${file.name} with class ${T::class.simpleName}: ${e.message ?: e.localizedMessage}",
            TAG
        )
        Logger.error(e.stackTraceToString(), TAG)
        null
    }
}