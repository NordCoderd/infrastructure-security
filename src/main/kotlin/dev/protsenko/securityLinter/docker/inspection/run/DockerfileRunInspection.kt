package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzerEP

class DockerfileRunInspection : LocalInspectionTool() {
    val extensionPointName =
        ExtensionPointName.create<DockerfileRunAnalyzerEP>("dev.protsenko.security-linter.dockerFileRunAnalyzer")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val extensions = extensionPointName.extensions

        return object : DockerVisitor() {
            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                val runCommand = element.text
                for (extension in extensions) {
                    extension.handle(runCommand, element, holder)
                }
            }
        }
    }
}