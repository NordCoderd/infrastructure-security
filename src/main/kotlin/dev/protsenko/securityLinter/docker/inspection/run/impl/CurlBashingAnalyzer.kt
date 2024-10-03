package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.CurlBashingValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer

class CurlBashingAnalyzer : DockerfileRunAnalyzer {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!CurlBashingValidator.isValid(runCommand)) {
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds027.avoid-curl-bashing"),
                ProblemHighlightType.ERROR
            )
        }
    }
}