package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer

class ArgumentsInRunCommandAnalyzer: DockerfileRunAnalyzer {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (psiElement !is DockerFileRunCommand) return
        if (psiElement.variableRefSimpleList.isNotEmpty()){
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds028.avoid-arg-in-run"),
                ProblemHighlightType.WARNING
            )
        }
    }
}