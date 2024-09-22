package dev.protsenko.securityLinter.docker.checker

object ZypperCleanChecker {
    private val installCommands = listOf(
        "install",
        "in",
        "remove",
        "rm",
        "source-install",
        "si",
        "patch"
    )

    private val installCommandsPattern = installCommands.joinToString("|") { Regex.escape(it) }

    val pattern = Regex(
        pattern = """^(RUN\s+)?(.*?\bzypper\s+($installCommandsPattern)\b)(?!.*\bzypper\s+(clean|cc)\b.*$).*""",
        options = setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
    )

    fun isValid(command: String) = !pattern.matches(command)

}
