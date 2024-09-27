package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS028ArgInRunCommandInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                if (element.variableRefSimpleList.isNotEmpty()){
                    holder.registerProblem(
                        element,
                        SecurityPluginBundle.message("ds028.avoid-arg-in-run"),
                        ProblemHighlightType.WARNING
                    )
                }
            }
        }
    }
}