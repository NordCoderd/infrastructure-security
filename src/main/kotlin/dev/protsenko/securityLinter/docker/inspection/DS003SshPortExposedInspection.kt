package dev.protsenko.securityLinter.docker.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileExposeCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerFileConstants.PROHIBITED_PORTS
import dev.protsenko.securityLinter.core.DockerPsiAnalyzer
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS003SshPortExposedInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object: DockerVisitor() {
            override fun visitDockerFileExposeCommand(element: DockerFileExposeCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size < 2 || commandParts.size > 3) return

                val fromPort = commandParts[1]

                if (PROHIBITED_PORTS.contains(fromPort)){
                    holder.registerProblem(element, SecurityPluginBundle.message("ds003.ssh-port-exposed"))
                }
            }
        }
    }
}