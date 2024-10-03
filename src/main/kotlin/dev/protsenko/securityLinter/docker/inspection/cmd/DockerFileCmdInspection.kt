package dev.protsenko.securityLinter.docker.inspection.cmd

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileCmdCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import dev.protsenko.securityLinter.core.quickFix.ReplaceWithJsonNotationQuickFix

class DockerFileCmdInspection: LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object: DockerVisitor(){
            val commands = mutableListOf<DockerFileCmdCommand>()

            override fun visitDockerFileCmdCommand(element: DockerFileCmdCommand) {
                commands.add(element)
                if (element.parametersInJsonForm == null){
                    holder.registerProblem(
                        element,
                        SecurityPluginBundle.message("ds031.use-json-notation"),
                        ProblemHighlightType.WARNING,
                        ReplaceWithJsonNotationQuickFix()
                    )
                }
            }

            override fun visitingIsFinished(file: PsiFile) {
                detectProblemAndDropCounters()
            }

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                detectProblemAndDropCounters()
            }

            private fun detectProblemAndDropCounters(){
                if (commands.size > 1){
                    for (command in commands.dropLast(1)) {
                        holder.registerProblem(
                            command,
                            SecurityPluginBundle.message("ds015.only-one-cmd"),
                            ProblemHighlightType.ERROR,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds015.remove-redundant-cmd"))
                        )
                    }
                }
                commands.clear()
            }
        }
    }


}