package domain.entities

import ru.astrainteractive.astralibs.orm.database.Column
import ru.astrainteractive.astralibs.orm.database.Entity
import ru.astrainteractive.astralibs.orm.database.Table

object UserRatingTable : Table<Int>("rating_user") {
    override val id: Column<Int> = integer("user_rating_id").primaryKey().autoIncrement()
    val userCreatedReport: Column<Int> = integer("user_created_report")
    val reportedUser: Column<Int> = integer("reported_user")
    val rating: Column<Int> = integer("rating")
    val message: Column<String> = text("message")
    val time: Column<String> = text("time")
}

class UserRating : Entity<Int>(UserRatingTable) {
    val id by UserRatingTable.id
    val userCreatedReport by UserRatingTable.userCreatedReport
    val reportedUser by UserRatingTable.reportedUser
    val rating by UserRatingTable.rating
    val message by UserRatingTable.message
    val time by UserRatingTable.time
}