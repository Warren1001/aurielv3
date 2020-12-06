package com.kabryxis.auriel.team

import discord4j.core.`object`.entity.Guild
import discord4j.core.`object`.entity.channel.Category

class TeamManager(private val guild: Guild) {
	
	private var category: Category? = null
	
	fun initialSetup() {
		category = guild.channels.filter { it is Category && it.name.toLowerCase() == "diablo reset" }.blockFirst() as Category?
	}
	
}