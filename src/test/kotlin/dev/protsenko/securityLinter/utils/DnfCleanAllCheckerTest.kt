package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.DnfCleanAllChecker
import junit.framework.TestCase

class DnfCleanAllCheckerTest : TestCase() {

    fun testValidCommands() {
        val commands = listOf(
            "RUN dnf install package && dnf clean all",
            "RUN dnf in package && dnf clean all",
            "RUN dnf reinstall package &&\n dnf clean all",
            "RUN dnf reinstall package &&\\\n dnf clean all",
            "RUN dnf rei package && dnf clean all",
            "RUN dnf install-n package && dnf clean all",
            "RUN dnf install-na package && dnf clean all",
            "RUN dnf install-nevra package && dnf clean all",
            "RUN echo 'Hello World'",
            "RUN dnf install package1 && dnf install package2 && dnf clean all",
            "RUN dnf install package && other_command && dnf clean all",
            "RUN dnf install package && dnf clean all && echo 'Some command'",
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid", DnfCleanAllChecker.isValid(command)
            )
        }
    }

    fun testInvalidCommands() {
        val commands = listOf(
            "RUN dnf install package",
            "RUN dnf in package",
            "RUN dnf reinstall package",
            "RUN dnf install package && echo 'No clean all'",
            "RUN dnf install package && dnf clean all && dnf install another_package",
            "RUN dnf clean all && dnf install package", // 'dnf clean all' before install
            "RUN dnf install package && dnf clean some_cache", // Incorrect clean command
            "RUN dnf install package && other_command",
            "RUN dnf in package && dnf update"
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be invalid", DnfCleanAllChecker.isValid(command)
            )
        }
    }
}
