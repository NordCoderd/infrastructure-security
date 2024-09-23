package dev.protsenko.securityLinter.docker.inspection.add

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.childrenOfType
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.PsiElementGenerator
import dev.protsenko.securityLinter.utils.extension
import dev.protsenko.securityLinter.utils.removeQuotes

class DS004AddInsteadCopyInspection : LocalInspectionTool() {
    companion object {
        const val GZ_FILE_EXTENSION = "gz"
        const val ADD_KEYWORD = "ADD"
        const val COPY_KEYWORD = "COPY"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {
                if (element.copyKeyword != null) return

                val filesFromJson =
                    element.parametersInJsonForm?.children?.map { it.text.removeQuotes() } ?: emptyList()
                val filesFromList = element.fileOrUrlList.map { it.text.removeQuotes() }

                val files = if (filesFromJson.isNotEmpty() && filesFromJson.size >= 2) {
                    filesFromJson.subList(0, filesFromJson.size - 1)
                } else if (filesFromList.isNotEmpty() && filesFromList.size >= 2) {
                    filesFromList.subList(0, filesFromList.size - 1)
                } else {
                    null
                } ?: return

                for (fileName in files) {
                    if (fileName.extension() != GZ_FILE_EXTENSION) {
                        holder.registerProblem(
                            element,
                            SecurityPluginBundle.message("ds004.add-instead-copy"),
                            ReplaceAddWithCopyQuickFix()
                        )
                        return
                    }
                }
            }
        }
    }

    private class ReplaceAddWithCopyQuickFix : LocalQuickFix {
        override fun getFamilyName(): @IntentionFamilyName String =
            SecurityPluginBundle.message("ds004.replace-add-with-copy")

        override fun applyFix(project: Project, problemDescriptor: ProblemDescriptor) {
            val problemElement = problemDescriptor.psiElement
            val addCommand = problemElement.text
            if (!addCommand.startsWith(ADD_KEYWORD)) return

            val copyCommand = addCommand.replaceFirst(ADD_KEYWORD, COPY_KEYWORD)
            runWriteAction {
                val copyPsiElement =
                    PsiElementGenerator.fromText<DockerFileAddOrCopyCommand>(project, copyCommand) ?: return@runWriteAction
                problemElement.replace(copyPsiElement)
            }
        }
    }


}