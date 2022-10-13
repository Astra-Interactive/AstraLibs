package ru.astrainteractive.astralibs.rest

import java.lang.reflect.Type

/**
 * Need to annotate your function with this annotation
 * @param path - path for your request ex: empireprojekt.ru/path
 * @param type the type of your request. Consider write it in uppercase; Possible types: GET, DELETE, PUT - default REST requests
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Request(val path: String, val type: String = "GET")


/**
 * Query for your url ex: empireprojekt.ru/?field=value
 * @param field the name of query parameter
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Query(val field: String)

/**
 * May be you will need something like empireprojekt.ru/{path}
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Path(val field: String)

/**
 * Default body request - need to pass any object - it will be converted to string with [RestRequester.Configuration.decoderFactory]
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Body

/**
 * This class is needed to implement concurrency because InvocationHandler is not multithreaded
 * @param await returns async part of InvocationHandler aka your converted json
 */
class ProxyTask<T>(val await: suspend () -> T?)

/**
 * Response for your requests
 * @param message returned message from server ex: OK etc
 * @param code returned code ex: 200, 404
 * @param response your converted class
 */
data class Response<T>(val message: String, val code: Int, val response: T?)

/**
 * Return normal package name ex: com.astrainteractive.SimpleClass
 */
val Type.fullPackageName: String
    get() = toString().replace("class ", "")
