package dev.protsenko.securityLinter.docker.checker

object DnfCleanAllChecker {
    private val installCommands = listOf(
        "install",
        "in",
        "reinstall",
        "rei",
        "install-n",
        "install-na",
        "install-nevra"
    )

    private val installCommandsPattern = installCommands.joinToString("|") { Regex.escape(it) }
    private val dnfInstallPattern = Regex("""\bdnf\s+($installCommandsPattern)\b""")

    private val dnfInstallWithoutCleanPattern = Regex(
        pattern = """(?s)^(RUN\s+)?(.*\b(dnf\s+($installCommandsPattern)\b)(?!.*\bdnf\s+clean\s+all\b).*)$""",
        options = setOf(RegexOption.IGNORE_CASE)
    )

    fun isValid(command: String): Boolean {
        if (dnfInstallPattern.containsMatchIn(command)) {
            if (dnfInstallWithoutCleanPattern.containsMatchIn(command)) {
                return false
            }
        }
        return true
    }
}