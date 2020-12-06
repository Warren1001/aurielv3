package com.kabryxis.auriel.command.impl

import com.kabryxis.auriel.Auriel
import com.kabryxis.auriel.command.Com
import com.kabryxis.auriel.command.CommandManager
import com.kabryxis.auriel.command.TextCommandHandler
import discord4j.core.`object`.entity.Message

@Com
class CommandCommand(private val commandManager: CommandManager) {
	
	@Com
	fun add(message: Message, arg: String) {
		if (Auriel.DEBUG) println("add was called")
		val args = arg.split(' ', ignoreCase = true, limit = 2)
		commandManager.registerCommand(TextCommandHandler(args[0].toLowerCase(), args[1]))
	}
	
	
}