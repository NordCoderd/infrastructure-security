package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.ZypperInstallWithoutCleanValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer

class ZypperInstallWithoutCleanAnalyzer : DockerfileRunAnalyzer {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!ZypperInstallWithoutCleanValidator.isValid(runCommand)) {
            holder.registerProblem(
                psiElement, SecurityPluginBundle.message("ds018.purge-zipper-cache"), ProblemHighlightType.WARNING
            )
        }
    }
}