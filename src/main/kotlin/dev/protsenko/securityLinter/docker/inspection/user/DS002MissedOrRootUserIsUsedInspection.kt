package dev.protsenko.securityLinter.docker.inspection.user

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileUserCommand
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer
import dev.protsenko.securityLinter.utils.PsiElementGenerator

class DS002MissedOrRootUserIsUsedInspection : LocalInspectionTool() {
    companion object {
        const val ROOT_USER = "root"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            var lastUser: String? = null
            var lastUserCommand: DockerFileUserCommand? = null
            var lastStage: DockerFileFromCommand? = null

            override fun visitDockerFileUserCommand(element: DockerFileUserCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size!=2) return
                lastUser = commandParts.lastOrNull() ?: return
                lastUserCommand = element
            }

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                lastUser = null
                lastUserCommand = null
                lastStage = element
            }

             override fun visitingIsFinished(file: PsiFile) {
                if (lastUser == null && lastStage != null){
                    holder.registerProblem(
                        lastStage!!,
                        SecurityPluginBundle.message("ds002.missing-user"),
                        ReplaceOrAddUserQuickFix(replace = false)
                    )
                } else if (lastUser == ROOT_USER) {
                    holder.registerProblem(
                        lastUserCommand!!,
                        SecurityPluginBundle.message("ds002.root-user-is-used"),
                        ReplaceOrAddUserQuickFix(replace = true)
                    )
                }
                 lastUser = null
                 lastUserCommand = null
                 lastStage = null
            }
        }
    }

    private class ReplaceOrAddUserQuickFix(private val replace: Boolean) : LocalQuickFix{
        companion object {
            const val USER_NOBODY = "USER nobody"
        }

        override fun getFamilyName(): @IntentionFamilyName String = if (replace) {
            SecurityPluginBundle.message("ds002.replace-root-with-nobody")
        } else {
            SecurityPluginBundle.message("ds002.add-nobody-user")
        }

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val nobodyElement = PsiElementGenerator.fromText<DockerFileUserCommand>(project, USER_NOBODY) ?: return
            ApplicationManager.getApplication().runWriteAction {
                if (replace) {
                    descriptor.psiElement.replace(nobodyElement)
                } else {
                    val newLine = PsiElementGenerator.newLine(project) ?: return@runWriteAction
                    val targetElement = descriptor.psiElement
                    val parentElement = targetElement.parent ?: return@runWriteAction
                    parentElement.add(newLine)
                    parentElement.add(nobodyElement)
                }
            }
        }
    }
}