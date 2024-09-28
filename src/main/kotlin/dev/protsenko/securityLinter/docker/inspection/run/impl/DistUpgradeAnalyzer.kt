package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.AptGetDistUpgradeValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzerEP

class DistUpgradeAnalyzer: DockerfileRunAnalyzerEP {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!AptGetDistUpgradeValidator.isValid(runCommand)){
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds022.no-dist-upgrade"),
                ProblemHighlightType.WARNING
            )
        }
    }
}