package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.factory.JavaObjectStreamFactory

/**
 * Default implementation for Java objects
 */
class JavaObjectEncoder : ObjectEncoder by DefaultObjectEncoder(JavaObjectStreamFactory)
