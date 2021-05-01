package com.kabryxis.auriel.command.impl

import com.kabryxis.auriel.Auriel
import com.kabryxis.auriel.command.Com
import discord4j.core.`object`.entity.Message

class DebugCommand {
	
	@Com
	fun debug(message: Message, arg: String) {
		Auriel.DEBUG = !Auriel.DEBUG
		message.channel.flatMap { it.createMessage("debug: ${Auriel.DEBUG}") }
	}
	
}