package dev.protsenko.securityLinter.docker.inspection.cmd_and_entrypoint

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileCmdCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileEntrypointCommand
import com.intellij.docker.dockerFile.parser.psi.DockerPsiExecOrShellCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import dev.protsenko.securityLinter.core.quickFix.ReplaceWithJsonNotationQuickFix

class DockerfileCmdAndEntrypointInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object: DockerfileVisitor(trackStages = true){
            val commands = mutableListOf<DockerFileCmdCommand>()
            val entryPoints = mutableListOf<DockerFileEntrypointCommand>()

            override fun visitDockerFileCmdCommand(element: DockerFileCmdCommand) {
                commands.add(element)
                if (element.parametersInJsonForm == null) registerJsonNotationProblem(element)
            }

            override fun visitDockerFileEntrypointCommand(element: DockerFileEntrypointCommand) {
                entryPoints.add(element)
                if (element.parametersInJsonForm == null) registerJsonNotationProblem(element)
            }

            override fun visitingIsFinished(file: PsiFile) {
                verifyInstructions(commands)
                verifyInstructions(entryPoints)
            }

            private fun verifyInstructions(elementList: List<DockerPsiExecOrShellCommand>){
                val lastStage = buildStages.keys.maxOrNull() ?: return
                val lastInstructions = elementList.filter {
                    it.textOffset > lastStage
                }
                if (lastInstructions.size > 1){
                    for (instruction in lastInstructions.dropLast(1)) {
                        holder.registerProblem(
                            instruction,
                            SecurityPluginBundle.message("ds015.only-one-cmd-or-entrypoint"),
                            ProblemHighlightType.ERROR,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds006.remove-redundant-instruction"))
                        )
                    }
                }
            }

            private fun registerJsonNotationProblem(element: DockerPsiExecOrShellCommand){
                holder.registerProblem(
                    element,
                    SecurityPluginBundle.message("ds031.use-json-notation"),
                    ProblemHighlightType.WARNING,
                    ReplaceWithJsonNotationQuickFix()
                )
            }
        }
    }


}