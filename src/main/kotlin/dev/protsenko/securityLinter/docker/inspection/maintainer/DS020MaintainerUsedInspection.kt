package dev.protsenko.securityLinter.docker.inspection.maintainer

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.parser.psi.DockerFileLabelCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileMaintainerCommand
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.PsiElementGenerator

class DS020MaintainerUsedInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileMaintainerCommand(element: DockerFileMaintainerCommand) {
                holder.registerProblem(
                    element,
                    SecurityPluginBundle.message("ds020.no-maintainer"),
                    ProblemHighlightType.LIKE_DEPRECATED,
                    ReplaceMaintainerWithLabel()
                )
            }
        }
    }

    private class ReplaceMaintainerWithLabel : LocalQuickFix {
        companion object {
            const val AUTHOR_LABEL = "LABEL org.opencontainers.image.authors=\""
        }

        override fun getFamilyName(): @IntentionFamilyName String = SecurityPluginBundle.message("ds020.replace-maintainer")

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val maintainer = descriptor.psiElement.text
            val authorText = maintainer.replace("MAINTAINER ", AUTHOR_LABEL).trim() + "\""
            ApplicationManager.getApplication().runWriteAction {
                val psiAuthor = PsiElementGenerator.fromText<DockerFileLabelCommand>(project, authorText) ?: return@runWriteAction
                descriptor.psiElement.replace(psiAuthor)
            }
        }
    }
}