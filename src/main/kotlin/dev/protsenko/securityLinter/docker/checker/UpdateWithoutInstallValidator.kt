package dev.protsenko.securityLinter.docker.checker

import dev.protsenko.securityLinter.docker.checker.core.RunCommandValidator

object UpdateWithoutInstallValidator: RunCommandValidator {
    val UPDATE_COMMANDS = setOf("update", "up")
    val INSTALL_COMMANDS = setOf(
        "upgrade", "install", "source-install", "reinstall", "groupinstall", "localinstall", "add"
    )
    val PACKAGE_MANAGERS = setOf(
        "apt-get", "apt", "yum", "apk", "dnf", "zypper"
    )

    // Create patterns with word boundaries
    val packageManagersPattern = PACKAGE_MANAGERS.joinToString("|") { "\\b${Regex.escape(it)}\\b" }
    val updateCommandsPattern = UPDATE_COMMANDS.joinToString("|") { "\\b${Regex.escape(it)}\\b(?![\\w\\-])" }
    val installCommandsPattern = INSTALL_COMMANDS.joinToString("|") { "\\b${Regex.escape(it)}\\b" }

    // Adjusted regex pattern
    val pattern = Regex(
        pattern = """^(RUN\s+)?($packageManagersPattern)\s+($updateCommandsPattern)(?!.*\b\2\s+($installCommandsPattern)\b)""",
        options = setOf(RegexOption.DOT_MATCHES_ALL, RegexOption.MULTILINE)
    )

    override fun isValid(command: String): Boolean {
        return !pattern.containsMatchIn(command)
    }
}
