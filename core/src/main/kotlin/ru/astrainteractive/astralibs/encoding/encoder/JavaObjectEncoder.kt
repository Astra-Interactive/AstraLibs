package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.factory.JavaObjectStreamFactory

/** [ObjectEncoder] using standard Java object serialization. */
class JavaObjectEncoder : ObjectEncoder by DefaultObjectEncoder(JavaObjectStreamFactory)
