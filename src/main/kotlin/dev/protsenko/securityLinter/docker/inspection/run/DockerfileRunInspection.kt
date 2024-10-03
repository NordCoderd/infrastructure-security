package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer
import dev.protsenko.securityLinter.utils.toStringDockerCommand

class DockerfileRunInspection : LocalInspectionTool() {
    val extensionPointName =
        ExtensionPointName.create<DockerfileRunAnalyzer>("dev.protsenko.security-linter.dockerFileRunAnalyzer")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val extensions = extensionPointName.extensions

        return object : DockerVisitor() {
            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                val execForm = element.parametersInJsonForm?.toStringDockerCommand("RUN ")

                val runCommand = if (execForm != null) {
                    execForm
                } else {
                    element.text
                }
                for (extension in extensions) {
                    extension.handle(runCommand, element, holder)
                }
            }
        }
    }
}