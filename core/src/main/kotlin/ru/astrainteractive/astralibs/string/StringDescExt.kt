package ru.astrainteractive.astralibs.string

object StringDescExt {
    fun StringDesc.map(block: (String) -> String): StringDesc {
        val raw = block.invoke(raw)
        return when (this) {
            is StringDesc.Raw -> StringDesc.Raw(raw)
            is StringDesc.Plain -> StringDesc.Plain(raw)
        }
    }

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
}
