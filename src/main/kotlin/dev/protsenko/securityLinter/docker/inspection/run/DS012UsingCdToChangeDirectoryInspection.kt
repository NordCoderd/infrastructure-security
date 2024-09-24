package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS012UsingCdToChangeDirectoryInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                val command = element.text
                if (!command.contains("cd")) return

                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size < 2) return
                if (commandParts[1] == "cd") {
                    holder.registerProblem(element, SecurityPluginBundle.message("ds012.use-workdir-over-cd"))
                }
            }
        }
    }
}