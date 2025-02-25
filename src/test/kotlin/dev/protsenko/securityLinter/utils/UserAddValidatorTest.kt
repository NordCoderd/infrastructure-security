package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.UserAddValidator
import junit.framework.TestCase

class UserAddValidatorTest : TestCase() {
    fun testValidUserAddCommands() {
        assertTrue(UserAddValidator.isValid("RUN useradd -l -u 123456 foobar"))
        assertTrue(UserAddValidator.isValid("RUN useradd -u -l 123456 foobar"))
        assertTrue(UserAddValidator.isValid("RUN useradd --no-log-init -u 654321 foobar"))
        assertTrue(UserAddValidator.isValid("RUN useradd -u --no-log-init 654321 foobar"))
        assertTrue(UserAddValidator.isValid("RUN echo 'hello world'") )
        assertTrue(UserAddValidator.isValid("RUN useradd -u 99999 foobar")) // Below threshold UID
    }

    fun testInvalidUserAddCommands() {
        assertFalse(UserAddValidator.isValid("RUN useradd -u 123456 foobar"))
        assertFalse(UserAddValidator.isValid("RUN useradd -u 1000000 foobar"))
        assertFalse(UserAddValidator.isValid("RUN useradd -u 1000001 foobar && echo 'done'"))
        assertFalse(UserAddValidator.isValid("RUN useradd -u 123456 -m foobar")) // `-m` doesn't fix the issue
    }
}
