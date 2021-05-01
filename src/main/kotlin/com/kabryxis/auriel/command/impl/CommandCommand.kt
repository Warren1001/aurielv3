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
		Auriel.debug("'command add' was called")
		val args = arg.split(' ', ignoreCase = true, limit = 2)
		val handler = TextCommandHandler(args[0].toLowerCase(), args[1])
		if (commandManager.registerCommand(handler)) {
			Auriel.getDatabase().set("commands(name,data)", handler.name, handler.template)
			message.channel.flatMap { it.createMessage("Registered the command '${handler.name}' with the message '${handler.template}'.") }.subscribe()
		} else {
			message.channel.flatMap { it.createMessage("A command already exists with the name '${handler.name}'.") }.subscribe()
		}
	}
	
	@Com(aliases = ["delete"])
	fun remove(message: Message, arg: String) {
		Auriel.debug("'command remove' was called")
		val name = arg.toLowerCase()
		if (commandManager.removeTextCommand(name)) {
			Auriel.getDatabase().delete("commands", "name='$name'")
			message.channel.flatMap { it.createMessage("Deleted the command '$name'.") }.subscribe()
		} else {
			message.channel.flatMap { it.createMessage("Unable to delete the command '$name'.") }.subscribe()
		}
	}
	
	
}