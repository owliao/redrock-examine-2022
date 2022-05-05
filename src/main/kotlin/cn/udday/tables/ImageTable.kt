package cn.udday.tables

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object ImageTable:LongIdTable("image") {
	var imageLink = varchar("image_link", 1000)
}

class ImageDao(id: EntityID<Long>) : LongEntity(id) {
	companion object : LongEntityClass<ImageDao>(ImageTable)

	var imageLink by ImageTable.imageLink
}