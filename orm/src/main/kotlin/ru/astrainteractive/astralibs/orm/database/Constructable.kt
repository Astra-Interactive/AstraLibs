package ru.astrainteractive.astralibs.orm.database

abstract class Constructable<T>(val construct: () -> T)
