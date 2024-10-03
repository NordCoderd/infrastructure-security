package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.SudoIsUsedValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer

class SudoIsUsedAnalyzer : DockerfileRunAnalyzer {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!SudoIsUsedValidator.isValid(runCommand)) {
            holder.registerProblem(
                psiElement, SecurityPluginBundle.message("ds009.run-using-sudo"), ProblemHighlightType.ERROR
            )
        }
    }
}