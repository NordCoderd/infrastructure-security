package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.SudoIsUsedValidator
import junit.framework.TestCase

class SudoCheckerTest : TestCase() {

    fun testisValid() {
        // Test cases where 'sudo' should be detected
        assertFalse(SudoIsUsedValidator.isValid("sudo apt-get update"))
        assertFalse(SudoIsUsedValidator.isValid("RUN sudo pip install --upgrade pip"))
        assertFalse(SudoIsUsedValidator.isValid("apt-get update && sudo apt-get install -y package"))
        assertFalse(SudoIsUsedValidator.isValid("bash -c 'sudo do_something'"))
        assertFalse(SudoIsUsedValidator.isValid("sudo"))
        assertFalse(SudoIsUsedValidator.isValid("   sudo   "))
        assertFalse(SudoIsUsedValidator.isValid("command && sudo other_command"))
    }

    fun testDoesNotContainSudo() {
        // Test cases where 'sudo' should not be detected
        assertTrue(SudoIsUsedValidator.isValid("apt-get update"))
        assertTrue(SudoIsUsedValidator.isValid("RUN apt-get install package"))
        assertTrue(SudoIsUsedValidator.isValid("echo 'This is a test'"))
        assertTrue(SudoIsUsedValidator.isValid("useradd sudoroot"))
        assertTrue(SudoIsUsedValidator.isValid("RUN echo sudoer"))
        assertTrue(SudoIsUsedValidator.isValid("command_sudo"))
        assertTrue(SudoIsUsedValidator.isValid("sudo_command"))
    }
}