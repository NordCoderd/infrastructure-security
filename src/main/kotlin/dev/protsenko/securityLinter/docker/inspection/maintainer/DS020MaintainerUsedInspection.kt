package dev.protsenko.securityLinter.docker.inspection.maintainer

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileMaintainerCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS020MaintainerUsedInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            override fun visitDockerFileMaintainerCommand(element: DockerFileMaintainerCommand) {
                holder.registerProblem(element, SecurityPluginBundle.message("ds020.no-maintainer"))
            }
        }
    }
}