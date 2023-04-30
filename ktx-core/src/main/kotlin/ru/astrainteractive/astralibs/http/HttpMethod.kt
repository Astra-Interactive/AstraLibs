package ru.astrainteractive.astralibs.http

sealed class HttpMethod(private val url: String) {
    private val arguments = mutableMapOf<String, Any?>()
    fun <T> param(key: String, value: T) {
        arguments[key] = value
    }

    val path: String
        get() = url + arguments.toList().joinToString("&", "?") { (q, p) -> "$q=$p" }

    class Post(
        url: String,
        var body: String = "",
    ) : HttpMethod(url)

    class Get(url: String) : HttpMethod(url)
}
