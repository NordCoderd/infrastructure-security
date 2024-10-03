package dev.protsenko.securityLinter.docker.inspection.from.core

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand

interface DockerfileFromAnalyzer {
    fun handle(element: DockerFileFromCommand, holder: ProblemsHolder)
}