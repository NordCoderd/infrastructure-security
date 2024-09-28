package dev.protsenko.securityLinter.docker.checker.core

interface RunCommandValidator {
    fun isValid(command: String): Boolean
}