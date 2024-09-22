package dev.protsenko.securityLinter.docker

fun String.removeQuotes(): String = this.replace("\"","").replace("'","")
fun String.extension(): String = this.substringAfterLast(".", "")