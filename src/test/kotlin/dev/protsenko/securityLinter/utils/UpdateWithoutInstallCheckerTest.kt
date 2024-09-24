package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.UpdateWithoutInstallChecker
import junit.framework.TestCase

class UpdateWithoutInstallCheckerTest : TestCase() {

    fun testUpdateWithInstall() {
        val commands = listOf(
            "RUN apt-get update && apt-get install -y package",
            "RUN apt update && apt install package",
            "RUN yum update && yum install package",
            "RUN apk update && apk add package",
            "RUN dnf update && dnf install package",
            "RUN zypper update && zypper install package",
            "RUN apt-get up && apt-get install package",
            "RUN apt-get update && echo 'Updating' && apt-get install package",
            "RUN yum up && yum reinstall package",
            "RUN dnf update && dnf groupinstall package",
            "RUN apt-get update && \\\n apt-get install -y screen"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid (contains update and install)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }

    fun testUpdateWithoutInstall() {
        val commands = listOf(
            "RUN apt-get update",
            "RUN apt update",
            "RUN yum update",
            "RUN apk update",
            "RUN dnf update",
            "RUN zypper update",
            "RUN apt-get up",
            "RUN yum up",
            "RUN apk up",
            "RUN dnf up",
            "RUN zypper up",
            "RUN apt-get update && echo 'No install command here'",
            "RUN yum update && echo 'Still no install'",
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be invalid (contains update without install)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }

    fun testUpdateWithDifferentManagerInstall() {
        val commands = listOf(
            "RUN apt-get update && yum install package",
            "RUN yum update && apt-get install package",
            "RUN apk update && dnf install package",
            "RUN dnf update && zypper install package",
            "RUN zypper update && apk add package"
        )
        for (command in commands) {
            // These should be invalid because install is with a different package manager
            assertFalse(
                "Command '$command' should be invalid (install with different package manager)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }

    fun testCommandsWithInterveningCommands() {
        val commands = listOf(
            "RUN apt-get update && echo 'Updating' && apt-get install package",
            "RUN yum update && sleep 5 && yum install package",
            "RUN apk up && echo 'Upgrading' && apk add package",
            "RUN dnf update && echo 'No install command'" // Should be invalid
        )
        val expectedResults = listOf(true, true, true, false)
        for ((command, expected) in commands.zip(expectedResults)) {
            val result = UpdateWithoutInstallChecker.isValid(command)
            assertEquals(
                "Command '$command' should be ${if (expected) "valid" else "invalid"}",
                expected,
                result
            )
        }
    }

    fun testNonUpdateCommands() {
        val commands = listOf(
            "RUN echo 'Hello World'",
            "RUN apt-get install package",
            "RUN yum reinstall package",
            "RUN apk add package",
            "RUN dnf groupinstall package",
            "RUN zypper localinstall package"
        )
        for (command in commands) {
            // These should be valid as they do not start with an update command
            assertTrue(
                "Command '$command' should be valid (no update command)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }

    fun testPartialMatches() {
        val commands = listOf(
            "RUN apt-get updatedb",
            "RUN yum updater",
            "RUN apk update-notifier",
            "RUN dnf updatenow",
            "RUN zypper update-manager"
        )
        for (command in commands) {
            // Should be valid as they do not match 'update' or 'up' exactly
            assertTrue(
                "Command '$command' should be valid (partial match should not trigger)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }

    fun testCommandsWithComments() {
        val commands = listOf(
            "RUN apt-get update && # apt-get install package",
            "RUN yum update && echo 'Update done' # yum install package", // Should be invalid
            "RUN apk update && apk add package # Install package", // Should be valid
            "RUN dnf update # && dnf install package" // Should be invalid
        )
        val expectedResults = listOf(false, false, true, false)
        for ((command, expected) in commands.zip(expectedResults)) {
            val result = UpdateWithoutInstallChecker.isValid(command)
            assertEquals(
                "Command '$command' should be ${if (expected) "valid" else "invalid"}",
                expected,
                result
            )
        }
    }

    fun testComplexCommands() {
        val commands = listOf(
            "apt-get update && apt-get install package && rm -rf /var/lib/apt/lists/*",
            "yum update && yum install package && yum clean all",
            "apk update && apk add package && rm -rf /var/cache/apk/*",
            "dnf update && dnf install package && dnf clean all",
            "zypper update && zypper install package && zypper clean -a"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid (complex command with install)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }

    fun testInvalidUpdateCommands() {
        val commands = listOf(
            "apt-get upgrade", // 'upgrade' is an install command, so should be valid
            "yum update && apt-get install package", // Install with different manager
            "apk up && apk" // Missing install command
        )
        val expectedResults = listOf(true, false, false)
        for ((command, expected) in commands.zip(expectedResults)) {
            val result = UpdateWithoutInstallChecker.isValid(command)
            assertEquals(
                "Command '$command' should be ${if (expected) "valid" else "invalid"}",
                expected,
                result
            )
        }
    }

    fun testCommandsWithoutPackageManagers() {
        val commands = listOf(
            "whois example.com",
            "curl -I https://www.google.com",
            "echo 'Hello, World!'",
            "python script.py",
            "node app.js",
            "mkdir /my/directory",
            "cp source.txt destination.txt",
            "rm -rf /tmp/*",
            "tar -czf archive.tar.gz /my/files",
            "service nginx start",
            "systemctl restart apache2",
            "ping -c 4 8.8.8.8",
            "grep 'pattern' file.txt",
            "sed -i 's/old/new/g' file.txt",
            "awk '{print $1}' file.txt"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid (no package manager commands)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }
}