package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.AptGetAutoYesValidator
import junit.framework.TestCase


class AptGetAutoYesCheckerTest : TestCase() {

    fun testValidCommands() {
        val commands = listOf(
            "RUN apt-get install -y package",
            "RUN apt-get -y install package",
            "RUN apt-get install package -y",
            "RUN apt-get install --yes package",
            "RUN apt-get install --assume-yes package",
            "RUN apt-get install -qy package",
            "RUN apt-get install -yq package",
            "RUN apt-get -qq -y install package",
            "RUN apt-get install package1 package2 -y",
            "RUN apt-get install -y package && \\\n apt-get install -y another-package",
            "RUN apt-get update && apt-get install -y package",
            "RUN apt-get install -y package1 && apt-get install -y package2",
            "RUN apt-get install -y package && echo 'Installation complete'",
            "RUN echo 'apt-get install package'",
            "RUN apt-get install -y package && apt-get clean",
            "RUN apt-get update; apt-get install -y package"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid",
                AptGetAutoYesValidator.isValid(command)
            )
        }
    }

    fun testInvalidCommands() {
        val commands = listOf(
            "RUN apt-get install package",
            "RUN apt-get install package1 package2",
            "RUN apt-get -qq install package",
            "RUN apt-get install package && apt-get clean",
            "RUN apt-get install package --no-install-recommends",
            "RUN apt-get update && apt-get install package",
            "RUN apt-get install -y package1 && apt-get install package2",
            "RUN apt-get install package1 && apt-get install -y package2",
            "RUN apt-get update && apt-get install package && apt-get install -y another-package",
            "RUN apt-get install -y package && apt-get install package2",
            "RUN apt-get update; apt-get install package",
            "RUN apt-get install package && echo 'Done'",
            "RUN apt-get -y install package && apt-get install package2"
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be invalid",
                AptGetAutoYesValidator.isValid(command)
            )
        }
    }
}
