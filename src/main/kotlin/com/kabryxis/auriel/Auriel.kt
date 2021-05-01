package com.kabryxis.auriel

import com.kabryxis.auriel.command.CommandManager
import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.reactor.mono
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {
	
	val token = args[0]
	val client = DiscordClient.create(token)
	
	client.withGateway {
		val commandManager = CommandManager()
		mono {
			it.on(MessageCreateEvent::class.java).doOnError(Throwable::printStackTrace).subscribe(MessageListener(commandManager))
			it.onDisconnect().subscribe { Auriel.getDatabase().close() }
		}
		
	}.block()
}

object Auriel {
	
	var DEBUG: Boolean = true
	
	private val timeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss:SS")
	private val database: Database = Database()
	
	fun getDatabase() : Database {
		return database
	}
	
	fun debug(msg: String) {
		if (DEBUG) println("[${LocalDateTime.now().format(timeFormat)}] $msg")
	}
	
}