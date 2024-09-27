package dev.protsenko.securityLinter.docker.inspection.entrypoint

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileEntrypointCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix

class DS006MultipleEntrypointInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            val entryPoints = mutableListOf<DockerFileEntrypointCommand>()

            override fun visitDockerFileEntrypointCommand(element: DockerFileEntrypointCommand) {
                entryPoints.add(element)
            }

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                detectProblemAndDropCounters()
            }

            override fun visitingIsFinished(file: PsiFile) {
                detectProblemAndDropCounters()
            }

            private fun detectProblemAndDropCounters() {
                if (entryPoints.size > 1) {
                    for (command in entryPoints.dropLast(1)) {
                        holder.registerProblem(
                            command,
                            SecurityPluginBundle.message("ds006.multiple-entrypoint"),
                            ProblemHighlightType.WARNING,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds006.remove-redundant-entrypoint"))
                        )
                    }
                }
                entryPoints.clear()
            }
        }
    }

}