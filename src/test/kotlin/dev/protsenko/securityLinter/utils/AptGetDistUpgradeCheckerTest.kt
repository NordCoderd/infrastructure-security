package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.AptGetDistUpgradeValidator
import junit.framework.TestCase

class AptGetDistUpgradeCheckerTest : TestCase() {

    fun testValidCommands() {
        val commands = listOf(
            "RUN apt-get update",
            "RUN apt-get install package",
            "RUN apt-get upgrade",
            "RUN apt-get update && apt-get install package"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid",
                AptGetDistUpgradeValidator.isValid(command)
            )
        }
    }

    fun testInvalidCommands() {
        val commands = listOf(
            "RUN apt-get dist-upgrade",
            "RUN apt-get update && apt-get dist-upgrade",
            "RUN apt-get \\\n dist-upgrade && apt-get install package",
            "RUN apt-get -y dist-upgrade",
            "RUN apt-get update && apt-get dist-upgrade && apt-get install package"
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be invalid",
                AptGetDistUpgradeValidator.isValid(command)
            )
        }
    }
}
