package dev.protsenko.securityLinter.utils

fun String.removeQuotes(): String = this.replace("\"","").replace("'","")
fun String.extension(): String = this.substringAfterLast(".", "")