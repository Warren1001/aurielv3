package com.kabryxis.auriel.command

import com.kabryxis.auriel.Auriel
import discord4j.core.`object`.entity.Message

class CommandManager {
	
	private val commandHandlers: MutableMap<String, CommandHandler> = mutableMapOf()
	
	fun registerCommands(listener: Any) {
		println("registerCommands called")
		if(listener.javaClass.isAnnotationPresent(Com::class.java)) {
			val commandHandler = SubCommandHandler(listener, listener.javaClass.getAnnotation(Com::class.java))
			if (Auriel.DEBUG) println("Found Com annotation on ${listener.javaClass.simpleName} class, constructing ${commandHandler.javaClass.simpleName}(${commandHandler.name})")
			commandHandlers[commandHandler.name] = commandHandler
		} else {
			listener.javaClass.declaredMethods.filter { it.isAnnotationPresent(Com::class.java) }.forEach {
				val commandHandler = SimpleCommandHandler(listener, listener.javaClass.getAnnotation(Com::class.java), it)
				if (Auriel.DEBUG) println("Found Com annotation on ${commandHandler.method.name} method, constructing ${commandHandler.javaClass.simpleName}(${commandHandler.name})")
				commandHandlers[commandHandler.name] = commandHandler
			}
		}
		
	}
	
	fun registerCommand(handler: TextCommandHandler) {
		if (Auriel.DEBUG) println("registerCommand called")
		commandHandlers[handler.name] = handler
	}
	
	fun handle(message: Message) {
		if (Auriel.DEBUG) println("handle called for content: '${message.content}'")
		val args = message.content.split(' ', ignoreCase = true, limit = 2)
		commandHandlers[args[0].substring(1).toLowerCase()]?.handle(message, if (args.size > 1) args[1] else "")
	}
	
	private fun register(commandHandler: AbstractCommandHandler) {
		commandHandlers[commandHandler.name] = commandHandler
		commandHandler.com.aliases.forEach { commandHandlers[it] = commandHandler }
	}

}