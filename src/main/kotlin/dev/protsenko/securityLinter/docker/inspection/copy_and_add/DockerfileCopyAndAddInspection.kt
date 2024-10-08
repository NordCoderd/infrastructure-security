package dev.protsenko.securityLinter.docker.inspection.copy_and_add

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.docker.inspection.copy_and_add.core.DockerfileCopyOrAddAnalyzer

class DockerfileCopyAndAddInspection: LocalInspectionTool() {
    val extensionPointName =
        ExtensionPointName.create<DockerfileCopyOrAddAnalyzer>("dev.protsenko.security-linter.dockerFileCopyOrAddAnalyzer")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        val extensions = extensionPointName.extensions

        return object : DockerfileVisitor(){
            override fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {
                for (extension in extensions) {
                    extension.handle(currentStep, element, holder)
                }
            }
        }
    }
}