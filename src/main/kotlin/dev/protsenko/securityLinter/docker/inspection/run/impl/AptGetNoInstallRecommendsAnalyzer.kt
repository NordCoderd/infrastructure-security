package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.AptGetNoInstallRecommendsValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzerEP

class AptGetNoInstallRecommendsAnalyzer:DockerfileRunAnalyzerEP {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!AptGetNoInstallRecommendsValidator.isValid(runCommand)){
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds030.use-no-install-recommends"),
                ProblemHighlightType.WARNING
            )
        }
    }
}