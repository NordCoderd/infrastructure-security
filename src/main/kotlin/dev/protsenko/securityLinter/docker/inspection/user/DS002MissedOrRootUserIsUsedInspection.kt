package dev.protsenko.securityLinter.docker.inspection.user

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileUserCommand
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer
import dev.protsenko.securityLinter.utils.PsiElementGenerator

class DS002MissedOrRootUserIsUsedInspection : LocalInspectionTool() {
    val prohibitedPorts = setOf("root", "0")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): DockerVisitor {
        return object : DockerVisitor(saveArguments = true) {
            var lastUser: String? = null
            var lastUserCommand: DockerFileUserCommand? = null
            var lastStage: DockerFileFromCommand? = null
            var isHighlighted = false

            override fun visitDockerFileUserCommand(element: DockerFileUserCommand) {
                val variables = element.variableRefSimpleList
                if (variables.isNotEmpty()) {
                    holder.registerProblem(
                        element,
                        SecurityPluginBundle.message("ds025.arg-in-user"),
                        ReplaceOrAddUserQuickFix(replace = true)
                    )
                    isHighlighted = true
                    lastUserCommand = element

                    //FIXME: Should work with many variables
                    val referencedName = variables.firstOrNull()?.referencedName?.text ?: return
                    lastUser = resolvedVariables[referencedName]
                } else {
                    val commandParts = DockerPsiAnalyzer.splitCommand(element)
                    if (commandParts.size != 2) return
                    lastUser = commandParts.lastOrNull() ?: return
                    lastUserCommand = element
                }
            }

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                lastUser = null
                lastUserCommand = null
                lastStage = element
            }

            override fun visitingIsFinished(file: PsiFile) {
                if (!isHighlighted && lastUser == null && lastStage != null) {
                    holder.registerProblem(
                        lastStage!!,
                        SecurityPluginBundle.message("ds002.missing-user"),
                        ReplaceOrAddUserQuickFix(replace = false)
                    )
                } else if (lastUser != null && prohibitedPorts.contains(lastUser)) {
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

    override fun inspectionFinished(session: LocalInspectionToolSession, problemsHolder: ProblemsHolder) {
        super.inspectionFinished(session, problemsHolder)
    }

    private class ReplaceOrAddUserQuickFix(private val replace: Boolean) : LocalQuickFix {
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