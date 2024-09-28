package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.DnfCleanAllValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzerEP

class MissingDnfCleanAnalyzer : DockerfileRunAnalyzerEP {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!DnfCleanAllValidator.isValid(runCommand)) {
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds017.purge-dnf-package-cache"),
                ProblemHighlightType.WARNING
            )
        }
    }
}