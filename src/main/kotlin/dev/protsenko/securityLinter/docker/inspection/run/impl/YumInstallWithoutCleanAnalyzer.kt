package dev.protsenko.securityLinter.docker.inspection.run.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.YumInstallWithoutCleanValidator
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer

class YumInstallWithoutCleanAnalyzer : DockerfileRunAnalyzer {
    override fun handle(runCommand: String, psiElement: PsiElement, holder: ProblemsHolder) {
        if (!YumInstallWithoutCleanValidator.isValid(runCommand)) {
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds014.purge-yum-package-cache"),
                ProblemHighlightType.WARNING
            )
        }
    }
}