package com.kabryxis.auriel.command

import com.kabryxis.auriel.Auriel
import discord4j.core.`object`.entity.Message
import java.lang.reflect.Method

class SimpleCommandHandler(listener: Any, com: Com, val method: Method) :
	AbstractCommandHandler(listener, com, if (com.name.isBlank()) method.name else com.name) {
	
	init {
		method.isAccessible = true
	}
	
	override fun handle(message: Message, arg: String) {
		if (Auriel.DEBUG) println("handle called for ${javaClass.simpleName}(${name})")
		method.invoke(listener, message, arg)
	}
	
}