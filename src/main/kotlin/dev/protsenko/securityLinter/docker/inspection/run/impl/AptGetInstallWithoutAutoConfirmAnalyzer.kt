package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.AptGetAutoYesValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzerEP

class AptGetInstallWithoutAutoConfirmAnalyzer: DockerfileRunAnalyzerEP {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!AptGetAutoYesValidator.isValid(runCommand)){
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds019.use-apt-auto-confirm"),
                ProblemHighlightType.WARNING
            )
        }
    }
}