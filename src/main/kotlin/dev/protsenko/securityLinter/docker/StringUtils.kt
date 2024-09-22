package dev.protsenko.securityLinter.utils

fun String.removeQuotes(): String = this.replace("\"","").replace("'","")