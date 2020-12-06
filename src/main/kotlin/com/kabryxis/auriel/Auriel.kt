package com.kabryxis.auriel

import com.kabryxis.auriel.command.CommandManager
import com.kabryxis.auriel.command.impl.CommandCommand
import discord4j.core.DiscordClient
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.reactor.mono

fun main(args: Array<String>) {
	val token = args[0]
	val client = DiscordClient.create(token)
	
	client.withGateway {
		val commandManager: CommandManager = CommandManager()
		commandManager.registerCommands(CommandCommand(commandManager))
		mono {
			it.on(MessageCreateEvent::class.java).doOnError(Throwable::printStackTrace).subscribe(MessageListener(commandManager))
		}
	}.block()
}

object Auriel {
	const val DEBUG: Boolean = true
}