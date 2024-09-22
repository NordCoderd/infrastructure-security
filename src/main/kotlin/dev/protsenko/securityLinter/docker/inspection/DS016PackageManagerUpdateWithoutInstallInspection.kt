package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.UpdateWithoutInstallChecker

class DS016PackageManagerUpdateWithoutInstallInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                val command = element.text.substringAfter("RUN ").trim()
                if (!UpdateWithoutInstallChecker.isValid(command)) {
                    holder.registerProblem(element, SecurityPluginBundle.message("ds016.no-orphan-package-update"))
                    return
                }
            }
        }
    }

}