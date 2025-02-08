package dev.protsenko.securityLinter.docker.inspection.copy_and_add.core

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand

interface DockerfileCopyOrAddAnalyzer {
    fun handle(element: DockerFileAddOrCopyCommand, holder: ProblemsHolder)
}