package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.PackageManagerAutoYesValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer

class PackageManagerAutoYesAnalyzer: DockerfileRunAnalyzer {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!PackageManagerAutoYesValidator.isValid(runCommand)){
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds019.use-package-manager-auto-confirm"),
                ProblemHighlightType.WARNING
            )
        }
    }
}