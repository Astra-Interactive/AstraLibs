package rating_test.database.domain.entities

import ru.astrainteractive.astralibs.orm.database.Column
import ru.astrainteractive.astralibs.orm.database.Constructable
import ru.astrainteractive.astralibs.orm.database.Entity
import ru.astrainteractive.astralibs.orm.database.Table

object SimpleUserTable : Table<Int>("user_table") {
    override val id: Column<Int> = integer("id").primaryKey().autoIncrement()
    val name: Column<String> = text("name").unique()
}

class SimpleUser : Entity<Int>(SimpleUserTable) {
    val id by SimpleUserTable.id
    var name by SimpleUserTable.name

    companion object : Constructable<SimpleUser>(::SimpleUser)
}