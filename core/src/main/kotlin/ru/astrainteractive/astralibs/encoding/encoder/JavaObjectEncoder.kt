package ru.astrainteractive.astralibs.encoding.encoder

import ru.astrainteractive.astralibs.encoding.factory.JavaObjectStreamFactory

/**
 * An [ObjectEncoder] implementation that uses standard Java object serialization
 * via [JavaObjectStreamFactory].
 *
 * Delegates all operations to [DefaultObjectEncoder], which handles the actual
 * serialization and deserialization logic.
 *
 * This encoder is suitable for environments where Java's native serialization is acceptable.
 */
class JavaObjectEncoder : ObjectEncoder by DefaultObjectEncoder(JavaObjectStreamFactory)
