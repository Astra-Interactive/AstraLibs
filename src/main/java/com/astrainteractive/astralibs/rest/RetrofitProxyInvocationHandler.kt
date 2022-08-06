package com.astrainteractive.astralibs.rest

import java.io.OutputStreamWriter
import java.lang.reflect.*
import java.net.HttpURLConnection
import java.net.URL

/**
 * Invocator for you REST requests
 * There may be other way to do that, but currently I don't know it, so this provided as is
 */
class RetrofitProxyInvocationHandler(private val configuration: RestRequester.Configuration) :
    InvocationHandler {

    private inline fun <reified T> getTransferAnnotation(method: Method): T? =
        method.annotations.firstNotNullOfOrNull { it as? T }


    private inline fun <reified T> annotationWithIndex(parameterAnnotations: kotlin.Array<out Array<out Annotation>>): List<Pair<Int, T & Any>> {
        var i = -1
        return parameterAnnotations.mapNotNull {
            i++
            it.firstNotNullOfOrNull { it as? T }?.let { i to it }
        }
    }

    private fun <T> annotationsToParam(indexed: List<Pair<Int, T>>, args: Array<out Any>?): List<Pair<Any, T>> {
        return indexed.mapNotNull { (i, annotation) ->
            args?.getOrNull(i)?.let { it to annotation }
        }
    }

    private fun buildQuery(method: Method, args: Array<out Any>?): String {
        return annotationsToParam(annotationWithIndex<Query>(method.parameterAnnotations), args).joinToString(
            "&",
            prefix = "?"
        ) {
            "${it.second.field}=${it.first}"
        }
    }

    private fun buildBody(method: Method, args: Array<out Any>?): Pair<Any, Body>? {
        return annotationsToParam(annotationWithIndex<Body>(method.parameterAnnotations), args).firstOrNull()
    }


    override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
        val request = getTransferAnnotation<Request>(method)
            ?: throw Exception("Classes which built with RestRequester should be annotated with Request annotation")

        val query: String = buildQuery(method, args)
        val body = buildBody(method, args)
        return ProxyTask {
            val url = URL(configuration.baseUrl + request.path + query)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = request.type
            configuration.headers().forEach(connection::setRequestProperty)
            body?.let {
                connection.doOutput = true
                connection.outputStream.also { os ->
                    OutputStreamWriter(os, "UTF-8").apply {
                        write(configuration.decoderFactory(it.first))
                        flush()
                        close()
                    }
                    os.close()
                }
            }

            val json = connection.run {
                connect()
                val json = this.url.readText()
                disconnect()
                json
            }
            val name =
                ((method.genericReturnType as ParameterizedType).actualTypeArguments[0] as ParameterizedType).actualTypeArguments[0].fullPackageName
            val clazz = Class.forName(name)
            val decoded = configuration.converterFactory.invoke(json, clazz)
            val code = connection.responseCode
            val message = connection.responseMessage
            Response(message, code, decoded)
        }
    }


}


