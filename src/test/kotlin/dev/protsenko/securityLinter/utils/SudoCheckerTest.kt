package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.SudoChecker
import junit.framework.TestCase

class SudoCheckerTest : TestCase() {

    fun testContainsSudo() {
        // Test cases where 'sudo' should be detected
        assertTrue(SudoChecker.containsSudo("sudo apt-get update"))
        assertTrue(SudoChecker.containsSudo("RUN sudo pip install --upgrade pip"))
        assertTrue(SudoChecker.containsSudo("apt-get update && sudo apt-get install -y package"))
        assertTrue(SudoChecker.containsSudo("bash -c 'sudo do_something'"))
        assertTrue(SudoChecker.containsSudo("sudo"))
        assertTrue(SudoChecker.containsSudo("   sudo   "))
        assertTrue(SudoChecker.containsSudo("command && sudo other_command"))
    }

    fun testDoesNotContainSudo() {
        // Test cases where 'sudo' should not be detected
        assertFalse(SudoChecker.containsSudo("apt-get update"))
        assertFalse(SudoChecker.containsSudo("RUN apt-get install package"))
        assertFalse(SudoChecker.containsSudo("echo 'This is a test'"))
        assertFalse(SudoChecker.containsSudo("useradd sudoroot"))
        assertFalse(SudoChecker.containsSudo("RUN echo sudoer"))
        assertFalse(SudoChecker.containsSudo("command_sudo"))
        assertFalse(SudoChecker.containsSudo("sudo_command"))
    }
}