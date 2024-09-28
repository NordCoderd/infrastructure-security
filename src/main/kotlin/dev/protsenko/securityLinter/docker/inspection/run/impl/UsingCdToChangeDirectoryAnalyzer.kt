package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzerEP
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer

class UsingCdToChangeDirectoryAnalyzer : DockerfileRunAnalyzerEP {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!runCommand.contains("cd")) return
        val commandParts = DockerPsiAnalyzer.splitCommand(runCommand)
        if (commandParts.size < 2) return
        if (commandParts[1] == "cd") {
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds012.use-workdir-over-cd"),
                ProblemHighlightType.INFORMATION
            )
        }
    }
}