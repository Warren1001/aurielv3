package com.kabryxis.auriel.command

import com.kabryxis.auriel.Auriel
import discord4j.core.`object`.entity.Message

class SubCommandHandler(listener: Any, com: Com) : AbstractCommandHandler(listener, com, if (com.name.isBlank()) getProperClassName(listener.javaClass) else com.name) {
	
	private val subCommands: MutableMap<String, CommandHandler> = mutableMapOf()
	
	init {
		listener.javaClass.declaredMethods.filter { it.isAnnotationPresent(Com::class.java) }
			.map { SimpleCommandHandler(listener, it.getAnnotation(Com::class.java), it) }.forEach {
				if (Auriel.DEBUG) println("Constructed ${it.javaClass.simpleName}(${it.name}) for ${javaClass.simpleName}(${name})")
				register(it)
			}
	}
	
	override fun handle(message: Message, arg: String) {
		if (Auriel.DEBUG) println("handle called for ${javaClass.simpleName}(${name})")
		val args = arg.split(" ", ignoreCase = true, limit = 2)
		subCommands[args[0].toLowerCase()]?.handle(message, if (args.size > 1) args[1] else "")
	}
	
	private fun register(commandHandler: AbstractCommandHandler) {
		subCommands[commandHandler.name] = commandHandler
		commandHandler.com.aliases.forEach { subCommands[it] = commandHandler }
	}
	
}

fun getProperClassName(clazz: Class<Any>) : String {
	var simpleName = clazz.simpleName.toLowerCase();
	if(simpleName.endsWith("command", ignoreCase = true)) simpleName = simpleName.substring(0, simpleName.length - 7)
	return simpleName
}