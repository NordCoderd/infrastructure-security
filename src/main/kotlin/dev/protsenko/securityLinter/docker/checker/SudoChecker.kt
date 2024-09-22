package dev.protsenko.securityLinter.utils

object SudoChecker {
    private val regex = Regex("\\bsudo\\b")

    fun containsSudo(command: String): Boolean {
        return regex.containsMatchIn(command)
    }
}
