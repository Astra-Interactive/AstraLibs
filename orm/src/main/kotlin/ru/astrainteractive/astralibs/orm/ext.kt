package ru.astrainteractive.astralibs.orm

internal fun <T> T.sqliteString(): T = if (this is String) "\"$this\"" as T else this