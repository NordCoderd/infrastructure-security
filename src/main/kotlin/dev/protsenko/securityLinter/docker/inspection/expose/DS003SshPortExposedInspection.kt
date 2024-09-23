package dev.protsenko.securityLinter.docker.inspection.expose

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileExposeCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerFileConstants.PROHIBITED_PORTS
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix

class DS003SshPortExposedInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileExposeCommand(element: DockerFileExposeCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size < 2 || commandParts.size > 3) return

                val fromPort = commandParts[1]

                if (PROHIBITED_PORTS.contains(fromPort)) {
                    holder.registerProblem(
                        element,
                        SecurityPluginBundle.message("ds003.ssh-port-exposed"),
                        DeletePsiElementQuickFix(SecurityPluginBundle.message("ds003.remove-dangerous-port-exposed"))
                    )
                }
            }
        }
    }
}