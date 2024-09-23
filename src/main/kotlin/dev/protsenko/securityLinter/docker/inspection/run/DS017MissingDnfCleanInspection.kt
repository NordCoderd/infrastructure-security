package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.checker.DnfCleanAllChecker

class DS017MissingDnfCleanInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                if (!DnfCleanAllChecker.isValid(element.text)){
                    holder.registerProblem(element, SecurityPluginBundle.message("ds017.purge-dnf-package-cache"))
                }
            }
        }
    }
}