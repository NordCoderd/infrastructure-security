package dev.protsenko.securityLinter.docker.checker

import dev.protsenko.securityLinter.docker.checker.core.RunCommandValidator

object AptGetDistUpgradeValidator: RunCommandValidator {
    private val pattern = Regex(
        pattern = """apt-get.*dist-upgrade""",
        options = setOf(RegexOption.IGNORE_CASE, RegexOption.DOT_MATCHES_ALL)
    )

    override fun isValid(command: String): Boolean = !pattern.containsMatchIn(command)
}
