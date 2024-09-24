package dev.protsenko.securityLinter.docker.inspection.user

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileUserCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer

class DS002MissedOrRootUserIsUsedInspection : LocalInspectionTool() {
    companion object {
        const val ROOT_USER = "root"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            var lastUser: String? = null
            var lastUserCommand: DockerFileUserCommand? = null

            override fun visitDockerFileUserCommand(element: DockerFileUserCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size!=2) return
                lastUser = commandParts.lastOrNull() ?: return
                lastUserCommand = element
            }

             override fun visitingIsFinished(file: PsiFile) {
                if (lastUser == null){
                    holder.registerProblem(file, SecurityPluginBundle.message("ds002.missing-user"))
                    return
                } else if (lastUser == ROOT_USER) {
                    holder.registerProblem(lastUserCommand!!, SecurityPluginBundle.message("ds002.root-user-is-used"))
                }
            }
        }
    }
}