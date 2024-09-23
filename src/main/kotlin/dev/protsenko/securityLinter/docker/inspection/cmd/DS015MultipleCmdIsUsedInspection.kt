package dev.protsenko.securityLinter.docker.inspection.cmd

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.parser.psi.DockerFileCmdCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import java.util.concurrent.atomic.AtomicInteger

class DS015MultipleCmdIsUsedInspection: LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object: DockerVisitor(){
            val cmdCount = AtomicInteger(0)
            val commands = mutableListOf<DockerFileCmdCommand>()

            override fun visitDockerFileCmdCommand(element: DockerFileCmdCommand) {
                commands.add(element)
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
                        holder.registerProblem(command,
                            SecurityPluginBundle.message("ds015.only-one-cmd"),
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds015.remove-redundant-cmd")))
                    }
                }

                cmdCount.set(0)
                commands.clear()
            }
        }
    }
}