package ru.astrainteractive.astralibs.file_manager

import java.io.File

interface IResourceProvider {
    fun isResourceExists(fileName: String): Boolean
    fun saveFromResource(fileName: String): File
}