package com.kabryxis.auriel.command

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Com(val name: String = "", val permission: String = "", val aliases: Array<String> = [])