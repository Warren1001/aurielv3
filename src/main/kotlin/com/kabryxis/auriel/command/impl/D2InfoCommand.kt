package com.kabryxis.auriel.command.impl

import com.kabryxis.auriel.Auriel
import com.kabryxis.auriel.command.Com
import discord4j.core.`object`.entity.Message

class D2InfoCommand {
	
	private val info: MutableMap<String, String> = mutableMapOf()
	private val aliases: MutableMap<String, String> = mutableMapOf()
	
	@Com
	fun d2info(message: Message, arg: String) {
		Auriel.debug("d2info was called")
		if (arg.startsWith("add ", ignoreCase = true)) {
			val args = arg.substring(4).split(" ", ignoreCase = true, limit = 2)
			val key = args[0].replace('_', ' ').toLowerCase()
			if (info.containsKey(key)) {
				message.channel.flatMap { it.createMessage("There is already information stored labeled as '$key'.") }.subscribe()
				return
			}
			Auriel.debug("added info: '$key' with '${args[1]}'")
			info[key] = args[1]
		} else if (arg.startsWith("alias ", ignoreCase = true)) {
			val args = arg.substring(6).split(" ", ignoreCase = true, limit = 3)
			if (args[0].equals("add", ignoreCase = true)) {
				val key = args[1].replace('_', ' ').toLowerCase()
				val value = args[2].replace('_', ' ').toLowerCase()
				Auriel.debug("adding alias '$key' to '$value'")
				if (aliases.containsKey(key)) Auriel.debug("overridding alias '$key' of '${aliases[key]}'")
				aliases[key] = value
			} else if (args[0].equals("remove", ignoreCase = true)) {
				val key = args[1].replace('_', ' ').toLowerCase()
				Auriel.debug("removing alias '$key'")
				if (aliases.remove(key) == null) Auriel.debug("could not find alias '$key'")
			}
		} else {
			if (info.containsKey(arg.toLowerCase())) {
				Auriel.debug("found info in info map")
				message.channel.flatMap { it.createMessage(info[arg.toLowerCase()]) }.subscribe()
			}
			else if (aliases.containsKey(arg.toLowerCase()) && info.containsKey(aliases[arg.toLowerCase()])) {
				Auriel.debug("found info through aliases")
				message.channel.flatMap { it.createMessage(info[aliases[arg.toLowerCase()]]) }.subscribe()
			}
		}
	}
	
}