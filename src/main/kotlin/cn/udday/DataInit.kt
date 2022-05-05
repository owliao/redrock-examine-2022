package cn.udday

import cn.udday.tables.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DataInit {
  fun init() {
    Database.connect(
      url = "jdbc:mysql://redrock.udday.cn:2025/examine?serverTimezone=Asia/Shanghai&characterEncoding=utf8&autoReconnect=true",
      driver = "com.mysql.cj.jdbc.Driver",
      user = "root",
      password = "020303",
    )
	  transaction {
		  SchemaUtils.create(
			  ColorTable,
			  ColorShadeTable,
			  ColorDetailTable,
			  ColorThemeTable,
			  ColorAndThemeTable,
			  IdeaTable,
			  IdeaDetailTable,
			  IdeaAndDetailTable,
			  ImageTable,
			  StarTable,
			  UserTable
		  )
	  }
  }
}