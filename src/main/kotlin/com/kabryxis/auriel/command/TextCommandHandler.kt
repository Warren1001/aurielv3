package com.kabryxis.auriel.command

import com.kabryxis.auriel.Auriel
import discord4j.core.`object`.entity.Message

class TextCommandHandler(val name: String, val template: String) : CommandHandler {
	
	override fun handle(message: Message, arg: String) {
		Auriel.debug("handle for TextCommandHandler($name) was called")
		message.channel.flatMap { it.createMessage(format(message, arg)) }.subscribe()
	}
	
	private fun format(message: Message, arg: String) : String {
		var msg = template;
		if (message.author.isPresent) msg = msg.replace("{user}", message.author.get().mention)
		if (arg.isNotBlank()) msg = msg.replace("{arg}", arg)
		return msg
	}
	
}