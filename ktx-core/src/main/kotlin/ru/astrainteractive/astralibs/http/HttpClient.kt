package ru.astrainteractive.astralibs.http

import java.io.*
import java.net.HttpURLConnection
import java.net.URL


/**
 * Very simple http client to perform some requests into network with java [URL]
 */
object HttpClient {
    fun get(url: String, builder: HttpMethod.Get.() -> Unit = {}): Result<String> = kotlin.runCatching {
        val method = HttpMethod.Get(url).apply(builder)
        val builtUrl = method.path
        val conn = URL(builtUrl).let(URL::openConnection)
        conn.getInputStream().reader().readLines().joinToString("")
    }

    fun post(url: String, builder: HttpMethod.Post.() -> Unit = {}): Result<String> = kotlin.runCatching {
        val method = HttpMethod.Post(url).apply(builder)
        val builtUrl = method.path
        val connection = URL(builtUrl).let(URL::openConnection) as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doInput = true
        connection.doOutput = true
        connection.useCaches = false
        if (method.body.isNotBlank()) {
            connection.setRequestProperty("Content-Type", "application/json; ; Charset=UTF-8")
            connection.setRequestProperty("Content-Length", method.body.length.toString())
            connection.outputStream.bufferedWriter(Charsets.UTF_8).write(method.body)
        }
        connection.inputStream.reader(Charsets.UTF_8).readLines().joinToString("")
    }
}