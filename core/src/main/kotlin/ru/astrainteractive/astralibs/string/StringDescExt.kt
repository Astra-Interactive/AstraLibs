package ru.astrainteractive.astralibs.string

/** Transforms the raw string via [block], preserving the concrete [StringDesc] subtype. */
fun StringDesc.map(block: (String) -> String): StringDesc {
    val raw = block.invoke(raw)
    return when (this) {
        is StringDesc.Raw -> StringDesc.Raw(raw)
        is StringDesc.Plain -> StringDesc.Plain(raw)
    }
}

/** Replaces all occurrences of [oldValue] with [newValue], preserving the subtype. */
fun StringDesc.replace(oldValue: String, newValue: String, ignoreCase: Boolean = false): StringDesc {
    val raw = raw.replace(
        oldValue = oldValue,
        newValue = newValue,
        ignoreCase = ignoreCase
    )
    return when (this) {
        is StringDesc.Raw -> StringDesc.Raw(raw)
        is StringDesc.Plain -> StringDesc.Plain(raw)
    }
}

/** Appends [other] to the raw value, preserving the subtype. */
operator fun StringDesc.plus(other: String): StringDesc {
    return when (this) {
        is StringDesc.Raw -> StringDesc.Raw(raw.plus(other))
        is StringDesc.Plain -> StringDesc.Plain(raw.plus(other))
    }
}

/** Concatenates raw values; result is [StringDesc.Raw] if either operand is raw, else [StringDesc.Plain]. */
operator fun StringDesc.plus(other: StringDesc): StringDesc {
    val isAnyRawStringDesc = this is StringDesc.Raw || other is StringDesc.Raw
    return when {
        isAnyRawStringDesc -> {
            StringDesc.Raw(this.raw.plus(other.raw))
        }

        else -> {
            StringDesc.Plain(this.raw.plus(other.raw))
        }
    }
}

/** Returns this descriptor if non-null, or an empty [StringDesc.Plain]. */
fun StringDesc?.orEmpty() = this ?: StringDesc.Plain("")

/** Returns this descriptor if non-null, or the result of [block]. */
fun StringDesc?.or(block: () -> StringDesc) = this ?: block.invoke()

/** Converts any [StringDesc] to a [StringDesc.Raw] preserving the raw string value. */
fun StringDesc.toRaw(): StringDesc.Raw {
    return StringDesc.Raw(this.raw)
}
