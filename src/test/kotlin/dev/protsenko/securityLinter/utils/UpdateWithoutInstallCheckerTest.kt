package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.UpdateWithoutInstallChecker
import junit.framework.TestCase

class UpdateWithoutInstallCheckerTest : TestCase() {

    fun testUpdateWithInstall() {
        val commands = listOf(
            "apt-get update && apt-get install -y package",
            "apt update && apt install package",
            "yum update && yum install package",
            "apk update && apk add package",
            "dnf update && dnf install package",
            "zypper update && zypper install package",
            "apt-get up && apt-get install package",
            "apt-get update && echo 'Updating' && apt-get install package",
            "yum up && yum reinstall package",
            "dnf update && dnf groupinstall package",
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
            "apt-get update",
            "apt update",
            "yum update",
            "apk update",
            "dnf update",
            "zypper update",
            "apt-get up",
            "yum up",
            "apk up",
            "dnf up",
            "zypper up",
            "apt-get update && echo 'No install command here'",
            "yum update && echo 'Still no install'",
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
            "apt-get update && yum install package",
            "yum update && apt-get install package",
            "apk update && dnf install package",
            "dnf update && zypper install package",
            "zypper update && apk add package"
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
            "apt-get update && echo 'Updating' && apt-get install package",
            "yum update && sleep 5 && yum install package",
            "apk up && echo 'Upgrading' && apk add package",
            "dnf update && echo 'No install command'" // Should be invalid
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
            "echo 'Hello World'",
            "apt-get install package",
            "yum reinstall package",
            "apk add package",
            "dnf groupinstall package",
            "zypper localinstall package"
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
            "apt-get updatedb",
            "yum updater",
            "apk update-notifier",
            "dnf updatenow",
            "zypper update-manager"
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
            "apt-get update && # apt-get install package",
            "yum update && echo 'Update done' # yum install package", // Should be invalid
            "apk update && apk add package # Install package", // Should be valid
            "dnf update # && dnf install package" // Should be invalid
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
            // Эти команды не должны вызывать срабатывание предупреждения
            assertTrue(
                "Command '$command' should be valid (no package manager commands)",
                UpdateWithoutInstallChecker.isValid(command)
            )
        }
    }
}