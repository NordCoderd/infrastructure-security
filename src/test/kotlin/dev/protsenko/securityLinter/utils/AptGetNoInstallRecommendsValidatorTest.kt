package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.AptGetNoInstallRecommendsValidator
import junit.framework.TestCase

class AptGetNoInstallRecommendsValidatorTest : TestCase() {

    fun testInvalidCommands() {
        val commands = listOf(
            "RUN apt-get install -y package",
            "RUN apt-get install package && some_other_command",
            "RUN echo 'Start' && apt-get install package",
            "RUN apt-get install -y package; other_command",
            "RUN \\\n apt-get install package",
            "RUN APT-GET INSTALL Package",
            "RUN apt-get    install    package",
            "RUN apt-get install package || echo 'Failed to install'",
            "RUN apt-get install package | bash",
            "RUN apt-get update && apt-get install package",
            "RUN apt-get clean all && apt-get install package",
            "RUN apt-get install -y python",
            "RUN apt-get update && apt-get -y install wget",
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be recognized as containing 'apt-get install' without '--no-install-recommends'",
                AptGetNoInstallRecommendsValidator.isValid(command)
            )
        }
    }

    fun testValidCommands() {
        val commands = listOf(
            "RUN apt-get install -y package --no-install-recommends",
            "RUN apt-get install package --no-install-recommends && apt-get clean",
            "RUN echo 'No install here'",
            "RUN apt-get INSTALL package --no-install-recommends",
            "RUN apt-get install package --no-install-recommends && some_command",
            "RUN apt-get install package --no-install-recommends; apt-get clean",
            "RUN apt-get clean all",
            "RUN apt-get install package --no-install-recommends || echo 'Installation succeeded'"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should not be recognized as containing 'apt-get install' without '--no-install-recommends'",
                AptGetNoInstallRecommendsValidator.isValid(command)
            )
        }
    }
}
