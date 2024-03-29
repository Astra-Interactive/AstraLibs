package ru.astrainteractive.astralibs.string

object StringDescExt {
    fun StringDesc.Raw.map(block: (String) -> String): StringDesc.Raw {
        return block.invoke(this.raw).let(StringDesc::Raw)
    }

    fun StringDesc.Raw.replace(oldValue: String, newValue: String, ignoreCase: Boolean = false): StringDesc.Raw {
        return this.raw.replace(
            oldValue = oldValue,
            newValue = newValue,
            ignoreCase = ignoreCase
        ).let(StringDesc::Raw)
    }
}
