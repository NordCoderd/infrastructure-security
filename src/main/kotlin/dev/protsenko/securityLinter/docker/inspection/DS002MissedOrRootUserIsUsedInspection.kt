package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileUserCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerFileInspectionConstants.PROHIBITED_USERS
import dev.protsenko.securityLinter.core.DockerPsiAnalyzer
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import java.util.concurrent.atomic.AtomicBoolean

class DS002MissedOrRootUserIsUsedInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            val userIsDeclared: AtomicBoolean = AtomicBoolean(false)

            override fun visitDockerFileUserCommand(element: DockerFileUserCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size!=2) return
                val declaredUser = commandParts.lastOrNull() ?: return
                userIsDeclared.set(true)
                if (PROHIBITED_USERS.contains(declaredUser)){
                    holder.registerProblem(element, SecurityPluginBundle.message("ds002.root-user-is-used"))
                }
            }

             override fun visitingIsFinished(file: PsiFile) {
                if (!userIsDeclared.get()){
                    holder.registerProblem(file, SecurityPluginBundle.message("ds002.missing-user"))
                }
            }

        }
    }
}