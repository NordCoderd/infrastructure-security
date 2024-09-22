package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS014YumInstallWithoutCleanInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                val runCommand = element.text
                //FIXME: There is could be a variations of how it called
                if (runCommand.contains("yum install") && !runCommand.contains("yum clean all")){
                    holder.registerProblem(element, SecurityPluginBundle.message("ds014.purge-yum-package-cache"))
                }
                super.visitDockerFileRunCommand(element)
            }
        }
    }
}