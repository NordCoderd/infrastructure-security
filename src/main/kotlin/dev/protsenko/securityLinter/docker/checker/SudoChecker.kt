package dev.protsenko.securityLinter.docker.checker

object SudoChecker {
    private val regex = Regex("\\bsudo\\b")

    fun containsSudo(command: String): Boolean {
        return regex.containsMatchIn(command)
    }
}
