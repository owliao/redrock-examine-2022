package cn.udday.tables

import cn.udday.tables.ColorTable.uniqueIndex
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull

object UserTable : LongIdTable("user") {
	val phoneNumber = long("phone_number").uniqueIndex()
	val name = varchar("name", 20).default("Redrocker")
}

class UserDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<UserDao>(UserTable)

	var phoneNumber by UserTable.phoneNumber
	var name by UserTable.name
}
