package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.DistUpgradeValidator
import junit.framework.TestCase

class DistUpgradeValidatorTest : TestCase() {

    fun testValidCommands() {
        val commands = listOf(
            // apt-get valid commands
            "RUN apt-get update",
            "RUN apt-get install package",
            "RUN apt-get upgrade",
            "RUN apt-get update && apt-get install package",
            // zypper valid commands
            "RUN zypper refresh",
            "RUN zypper install package",
            "RUN zypper update",
            "RUN zypper refresh && zypper install package",
            "RUN zypper install -y package",
            "RUN zypper up",
            "RUN zypper in package",
            "RUN zypper refresh && zypper up",
            "RUN zypper install duplicate",
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid",
                DistUpgradeValidator.isValid(command)
            )
        }
    }

    fun testInvalidCommands() {
        val commands = listOf(
            // apt-get invalid commands
            "RUN apt-get dist-upgrade",
            "RUN apt-get update && apt-get dist-upgrade",
            "RUN apt-get \\\n dist-upgrade && apt-get install package",
            "RUN apt-get -y dist-upgrade",
            "RUN apt-get update && apt-get dist-upgrade && apt-get install package",
            // zypper invalid commands
            "RUN zypper dist-upgrade",
            "RUN zypper refresh && zypper dist-upgrade",
            "RUN zypper \\\n dist-upgrade && zypper install package",
            "RUN zypper -y dist-upgrade",
            "RUN zypper refresh && zypper dist-upgrade && zypper install package",
            "RUN zypper dup",
            "RUN zypper --non-interactive dist-upgrade",
            "RUN zypper dist-upgrade --no-confirm"
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be invalid",
                DistUpgradeValidator.isValid(command)
            )
        }
    }
}
