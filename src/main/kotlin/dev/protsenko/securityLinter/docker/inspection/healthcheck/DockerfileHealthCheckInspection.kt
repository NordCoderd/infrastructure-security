package dev.protsenko.securityLinter.docker.inspection.healthcheck

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileHealthCheckCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix

class DockerfileHealthCheckInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerfileVisitor(trackStages = true){
            val healthChecks = mutableListOf<DockerFileHealthCheckCommand>()

            override fun visitDockerFileHealthCheckCommand(element: DockerFileHealthCheckCommand) {
                healthChecks.add(element)
            }

            override fun visitingIsFinished(file: PsiFile) {
                val lastStage = buildStages.keys.maxOrNull() ?: return
                val lastInstructions = healthChecks.filter {
                    it.textOffset > lastStage
                }
                if (lastInstructions.size > 1){
                    for (instruction in lastInstructions.dropLast(1)) {
                        holder.registerProblem(
                            instruction,
                            SecurityPluginBundle.message("ds021.only-one-healthcheck"),
                            ProblemHighlightType.ERROR,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds021.remove-redundant-healthcheck"))
                        )
                    }
                }
            }
        }
    }
}