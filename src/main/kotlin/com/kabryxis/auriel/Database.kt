package com.kabryxis.auriel

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet

class Database {
	
	private val connection: Connection = DriverManager.getConnection("jdbc:sqlite:C://sqlite/db/test.db")
	
	fun createTable(name: String, vararg args: String) {
		
		val builder = StringBuilder("CREATE TABLE IF NOT EXISTS ")
		builder.append(name).append(" (")
		args.forEachIndexed { i, entry ->
			builder.append(entry)
			if (i != args.size - 1) builder.append(",")
		}
		builder.append(");")
		
		try {
			connection.createStatement().use {
				it.execute(builder.toString())
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	fun forEachOfSet(selection: String, table: String, action: (ResultSet) -> Unit) {
		try {
			connection.createStatement().use {
				it.executeQuery("SELECT $selection FROM $table").use { set ->
					while (set.next()) {
						action(set)
					}
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	fun set(insert: String, vararg values: Any) {
		val builder = StringBuilder("?")
		for (i in values.size downTo 2) builder.append(",?")
		try {
			connection.prepareStatement("INSERT INTO $insert VALUES($builder)").use {
				values.forEachIndexed { index, o -> it.setObject(index + 1, o) }
				it.executeUpdate()
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	fun delete(table: String, condition: String) {
		try {
			connection.createStatement().use {
				val cmd = "DELETE FROM $table WHERE $condition"
				Auriel.debug("executing sql command (delete): $cmd")
				it.execute(cmd)
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}
	}
	
	fun close() {
		Auriel.debug("closing sqlite connection")
		connection.close()
	}

}