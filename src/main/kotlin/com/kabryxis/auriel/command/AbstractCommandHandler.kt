package com.kabryxis.auriel.command

abstract class AbstractCommandHandler(val listener: Any, val com: Com, val name: String) : CommandHandler