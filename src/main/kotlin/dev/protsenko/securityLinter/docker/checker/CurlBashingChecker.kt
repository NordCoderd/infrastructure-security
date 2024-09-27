package dev.protsenko.securityLinter.docker.checker

object CurlBashingChecker {
    val curlBashingRegex = Regex("RUN.*(curl|wget)[^|^>]*[|>]")

    fun isValid(command: String): Boolean = !curlBashingRegex.containsMatchIn(command)
}