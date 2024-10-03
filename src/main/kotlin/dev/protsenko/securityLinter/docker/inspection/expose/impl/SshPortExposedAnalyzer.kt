package dev.protsenko.securityLinter.docker.inspection.expose.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.DockerFileConstants.PROHIBITED_PORTS
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import dev.protsenko.securityLinter.docker.inspection.expose.core.DockerfileExposeAnalyzer

class SshPortExposedAnalyzer : DockerfileExposeAnalyzer {
    override fun handle(ports: List<Int>, psiElement: PsiElement, holder: ProblemsHolder) {
        val prohibitedPortsIsExposed = ports.any {
            PROHIBITED_PORTS.contains(it)
        }

        if (prohibitedPortsIsExposed) {
            holder.registerProblem(
                psiElement,
                SecurityPluginBundle.message("ds003.ssh-port-exposed"),
                ProblemHighlightType.ERROR,
                DeletePsiElementQuickFix(SecurityPluginBundle.message("ds003.remove-dangerous-port-exposed"))
            )
        }
    }
}