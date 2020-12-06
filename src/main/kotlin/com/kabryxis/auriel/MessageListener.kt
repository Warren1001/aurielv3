package com.kabryxis.auriel

import com.kabryxis.auriel.command.CommandManager
import discord4j.core.event.domain.message.MessageCreateEvent
import kotlinx.coroutines.reactor.mono
import java.util.function.Consumer

class MessageListener(private val commandManager: CommandManager) : Consumer<MessageCreateEvent> {
	
	override fun accept(e: MessageCreateEvent) {
		mono { e.message }.filter { it.content[0] == '!' }.subscribe(commandManager::handle)
	}
	
}