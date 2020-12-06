package com.kabryxis.auriel.command

import discord4j.core.`object`.entity.Message

interface CommandHandler {
	
	fun handle(message: Message, arg: String)
	
}