package com.kabryxis.auriel.command

import com.kabryxis.auriel.Auriel
import com.kabryxis.auriel.command.impl.CommandCommand
import com.kabryxis.auriel.command.impl.D2InfoCommand
import com.kabryxis.auriel.command.impl.DebugCommand
import discord4j.core.`object`.entity.Message
import java.sql.ResultSet

class CommandManager {
	
	private val commandHandlers: MutableMap<String, CommandHandler> = mutableMapOf()
	
	init {
		registerCommands(CommandCommand(this))
		registerCommands(D2InfoCommand())
		registerCommands(DebugCommand())
		Auriel.getDatabase().createTable("commands", "name text PRIMARY KEY", "data text NOT NULL")
		registerSavedTextCommands()
	}
	
	private fun registerSavedTextCommands() {
		Auriel.getDatabase().forEachOfSet("*", "commands") { set: ResultSet ->
			val handler = TextCommandHandler(set.getString("name"), set.getString("data"))
			commandHandlers[handler.name] = handler
			Auriel.debug("Registered TextCommandHandler(${handler.name})")
		}
	}
	
	fun registerCommands(listener: Any) {
		Auriel.debug("registerCommands called for listener ${listener.javaClass.simpleName}")
		if (listener.javaClass.isAnnotationPresent(Com::class.java)) {
			val commandHandler = SubCommandHandler(listener, listener.javaClass.getAnnotation(Com::class.java))
			Auriel.debug("Found Com annotation on ${listener.javaClass.simpleName} class, constructing ${commandHandler.javaClass.simpleName}(${commandHandler.name})")
			commandHandlers[commandHandler.name] = commandHandler
		} else {
			listener.javaClass.declaredMethods.filter { it.isAnnotationPresent(Com::class.java) }.forEach {
				val commandHandler = SimpleCommandHandler(listener, it.getAnnotation(Com::class.java), it)
				Auriel.debug("Found Com annotation on ${commandHandler.method.name} method, constructing ${commandHandler.javaClass.simpleName}(${commandHandler.name})")
				commandHandlers[commandHandler.name] = commandHandler
			}
		}
		
	}
	
	fun registerCommand(handler: TextCommandHandler): Boolean {
		Auriel.debug("registerCommand called for TextCommandHandler(${handler.name})")
		if(commandHandlers.containsKey(handler.name)) {
			Auriel.debug("tried to register TextCommandHandler named '${handler.name}', already exists")
			return false
		}
		commandHandlers[handler.name] = handler
		return true
	}
	
	fun removeTextCommand(name: String): Boolean {
		Auriel.debug("removeTextCommand called")
		if (commandHandlers.containsKey(name)) {
			if (commandHandlers[name] is TextCommandHandler) {
				commandHandlers.remove(name)
				return true
			} else Auriel.debug("tried to remove CommandHandler named '$name', was not a TextCommandHandler")
		} else Auriel.debug("failed to find TextCommandHandler named '$name' to remove")
		return false
	}
	
	fun handle(message: Message) {
		Auriel.debug("handle called for content: '${message.content}'")
		val args = message.content.split(' ', ignoreCase = true, limit = 2)
		commandHandlers[args[0].substring(1).toLowerCase()]?.handle(message, if (args.size > 1) args[1] else "")
	}
	
	private fun register(commandHandler: AbstractCommandHandler) {
		commandHandlers[commandHandler.name] = commandHandler
		commandHandler.com.aliases.forEach { commandHandlers[it] = commandHandler }
	}
	
}