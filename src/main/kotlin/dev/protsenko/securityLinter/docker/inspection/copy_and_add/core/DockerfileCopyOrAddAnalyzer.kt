package dev.protsenko.securityLinter.docker.inspection.copy_and_add.core

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand

interface DockerfileCopyOrAddAnalyzer {
    fun handle(currentStep: String?, element: DockerFileAddOrCopyCommand, holder: ProblemsHolder)
}