package dev.protsenko.securityLinter.docker.inspection.copy

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.removeQuotes

class DS010UseSlashForCopyArgsInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {
                val parametersInJsonForm = element.parametersInJsonForm
                val fileOrUrlList = element.fileOrUrlList

                val lastCopyArgument = if (parametersInJsonForm != null){
                    val copyArguments = parametersInJsonForm.children
                    if (copyArguments.size <= 2) return
                    copyArguments.last()
                } else if (!fileOrUrlList.isEmpty()){
                    if (fileOrUrlList.size <= 2) return
                    fileOrUrlList.last()
                } else {
                    null
                }?: return

                val lastElement = lastCopyArgument.text?.removeQuotes() ?: return
                if (!lastElement.endsWith("/") && ! lastElement.endsWith("\\")) {
                    holder.registerProblem(
                        element,
                        SecurityPluginBundle.message("ds010.use-slash-for-copy-args"),
                        ProblemHighlightType.ERROR
                    )
                }
            }
        }
    }
}