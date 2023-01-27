package ru.astrainteractive.astralibs.orm.query

interface Query {
    fun generate(): String
    companion object{
        val Count = ::CountQuery
        val Create = ::CreateQuery
        val Delete = ::DeleteQuery
        val Insert = ::InsertQuery
        val Select = ::SelectQuery
        val Update = ::UpdateQuery
    }
}

