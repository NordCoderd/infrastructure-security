package dev.protsenko.securityLinter.docker.checker


object AptGetAutoYesChecker {
    private val separators = Regex("""\s*(?:&&|;|\n)\s*""")

    private val assumeYesFlagsPattern = Regex(
        pattern = """\s(-{1,2}yes\b|--assume-yes\b|-[^\s]*y[^\s]*)""",
        options = setOf(RegexOption.IGNORE_CASE)
    )

    private val aptGetInstallCommandPattern = Regex(
        pattern = """^\s*apt-get(?:\s+\S+)*\s+install\b""",
        options = setOf(RegexOption.IGNORE_CASE)
    )

    fun isValid(command: String): Boolean {
        if (!command.contains("apt-get")) return true
        val cmd = command.removePrefix("RUN").trim()
        val individualCommands = cmd.split(separators)

        for (individualCommand in individualCommands) {
            val cmdTrimmed = individualCommand.trim()
            if (aptGetInstallCommandPattern.containsMatchIn(cmdTrimmed)) {
                if (!assumeYesFlagsPattern.containsMatchIn(cmdTrimmed)) {
                    return false
                }
            }
        }
        return true
    }
}
