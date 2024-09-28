package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.CurlBashingValidator
import junit.framework.TestCase

class CurlBashingCheckerTest : TestCase() {

    fun testInvalidCommands() {
        val commands = listOf(
            "RUN curl -s https://example.com | bash",
            "RUN wget -q https://example.com \n| bash",
            "RUN curl -o- https://example.com \\n> bash",
            "RUN wget https://example.com \\\n| sh"
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be invalid for curl bashing",
                CurlBashingValidator.isValid(command)
            )
        }
    }

    fun testValidCommands() {
        val commands = listOf(
            "RUN echo Hello World",
            "RUN curl -s https://example.com",
            "RUN wget -q https://example.com",
            "RUN echo 'Some command' | bash"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid for curl bashing",
                CurlBashingValidator.isValid(command)
            )
        }
    }
}
