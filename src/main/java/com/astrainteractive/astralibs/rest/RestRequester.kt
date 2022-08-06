package com.astrainteractive.astralibs.rest

import java.lang.reflect.Proxy

/**
 * Very lightweight REST api provider
 * You can use it as retrofit if you familiar with this library
 * If you are not fully understand how that works - you can see plugin template https://github.com/Astra-Interactive/AstraTemplate
 * Or https://github.com/makeevrserg/SimpleRoom
 */
class RestRequester(private val configuration: Configuration.() -> Unit) {
    /**
     * [baseUrl] - base url of your api
     * [headers] - builder for headers
     * [converterFactory] - will convert json string to your class
     * [decoderFactory] - will convert Object to String - used to create [Body]
     */
    data class Configuration(
        var baseUrl: String = "",
        var headers: () -> Map<String, String> = { emptyMap() },
        var converterFactory: (String?, Class<*>) -> Any? = { _, _ -> },
        var decoderFactory: (Any?) -> String = { "" },
    )

    /**
     * In [T] you need to pass your interface class with REST api requests
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> create(clazz: Class<T>): T {
        val configuration = Configuration().apply { configuration() }
        return Proxy.newProxyInstance(
            clazz.classLoader, arrayOf(clazz),
            RetrofitProxyInvocationHandler(configuration)
        ) as T
    }
}

