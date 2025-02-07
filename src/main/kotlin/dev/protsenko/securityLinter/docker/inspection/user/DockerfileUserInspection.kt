package dev.protsenko.securityLinter.docker.inspection.user

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalInspectionToolSession
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.DockerPsiFile
import com.intellij.docker.dockerFile.parser.psi.DockerFileUserCommand
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiElementVisitor.EMPTY_VISITOR
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerFileConstants.PROHIBITED_USERS
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer
import dev.protsenko.securityLinter.utils.PsiElementGenerator
import dev.protsenko.securityLinter.utils.resolveVariable

class DockerfileUserInspection : LocalInspectionTool() {
    override fun buildVisitor(
        holder: ProblemsHolder,
        isOnTheFly: Boolean,
        session: LocalInspectionToolSession
    ): PsiElementVisitor {
        if (holder.file !is DockerPsiFile) {
            return EMPTY_VISITOR
        }
        val resolvedUsers = mutableMapOf<Int, DockerFileUserCommand>()

        return object : DockerfileVisitor(trackStages = true) {
            override fun visitDockerFileUserCommand(element: DockerFileUserCommand) {
                resolvedUsers[element.textOffset] = element
            }

            override fun visitingIsFinished(file: PsiFile) {
                val lastStageOffset = buildStages.keys.maxOrNull() ?: return
                val lastUserOffset = resolvedUsers.keys.maxOrNull()

                val lastStage = buildStages[lastStageOffset] ?: return
                if (lastUserOffset == null || lastUserOffset < lastStageOffset) {
                    holder.registerProblem(
                        lastStage,
                        SecurityPluginBundle.message("ds002.missing-user"),
                        ProblemHighlightType.ERROR,
                        ReplaceOrAddUserQuickFix(replace = false)
                    )
                    return
                }

                val lastUser = resolvedUsers[lastUserOffset] ?: return
                val variables = lastUser.variableRefSimpleList

                val username = if (variables.isNotEmpty()) {
                    holder.registerProblem(
                        lastUser,
                        SecurityPluginBundle.message("ds025.arg-in-user"),
                        ProblemHighlightType.ERROR,
                        ReplaceOrAddUserQuickFix(replace = true)
                    )
                    val referencedNameVariable = variables.firstOrNull() ?: return
                    referencedNameVariable.resolveVariable()
                } else {
                    val commandParts = DockerPsiAnalyzer.splitCommand(lastUser)
                    if (commandParts.size != 2) return
                    commandParts.lastOrNull()
                }

                if (username != null && PROHIBITED_USERS.contains(username)) {
                    holder.registerProblem(
                        lastUser,
                        SecurityPluginBundle.message("ds002.root-user-is-used"),
                        ProblemHighlightType.ERROR,
                        ReplaceOrAddUserQuickFix(replace = true)
                    )
                }
            }
        }
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
            if (replace) {
                descriptor.psiElement.replace(nobodyElement)
            } else {
                val newLine = PsiElementGenerator.newLine(project) ?: return
                val targetElement = descriptor.psiElement
                val parentElement = targetElement.parent ?: return
                parentElement.add(newLine)
                parentElement.add(nobodyElement)
            }
        }
    }
}