package dev.protsenko.securityLinter.docker.checker

object AptGetDistUpgradeChecker {
    private val pattern = Regex(
        pattern = """apt-get.*dist-upgrade""",
        options = setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
    )

    fun isValid(command: String): Boolean = !pattern.containsMatchIn(command)
}
