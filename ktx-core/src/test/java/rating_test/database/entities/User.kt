package rating_test.database.entities

import ru.astrainteractive.astralibs.database_v2.Column
import ru.astrainteractive.astralibs.database_v2.Entity
import ru.astrainteractive.astralibs.database_v2.Table

object UserTable : Table<Int>("rating_user") {
    override val id: Column<Int> = integer("user_id").primaryKey().autoIncrement()
    val uuid: Column<String> = text("minecraftUUID").unique()
    val lastUpdated: Column<Long> = long("lastUpdated")
}

class User : Entity<Int>(UserTable) {
    val id by UserTable.id
    var uuid by UserTable.uuid
    val lastUpdated by UserTable.lastUpdated
}