package dev.protsenko.securityLinter.docker.inspection.healthcheck

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileHealthCheckCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix

class DockerfileHealthCheckInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            val healthChecks = mutableListOf<DockerFileHealthCheckCommand>()

            override fun visitDockerFileHealthCheckCommand(element: DockerFileHealthCheckCommand) {
                healthChecks.add(element)
            }

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                detectProblemAndDropCounters()
            }

            override fun visitingIsFinished(file: PsiFile) {
                detectProblemAndDropCounters()
            }

            private fun detectProblemAndDropCounters() {
                if (healthChecks.size > 1) {
                    for (command in healthChecks.dropLast(1)) {
                        holder.registerProblem(
                            command,
                            SecurityPluginBundle.message("ds021.only-one-healthcheck"),
                            ProblemHighlightType.WARNING,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds021.remove-redundant-healthcheck"))
                        )
                    }
                }
                healthChecks.clear()
            }
        }
    }
}