package dev.protsenko.securityLinter.docker.inspection.workdir

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileWorkdirCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.AbsolutePathResolver

class DockerfileWorkdirInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerfileVisitor(){
            override fun visitDockerFileWorkdirCommand(element: DockerFileWorkdirCommand) {
                val workdirPath = element.fileOrUrlList.firstOrNull()?.text ?: return

                if (!AbsolutePathResolver.isAbsolutePath(workdirPath)){
                    holder.registerProblem(
                        element,
                        SecurityPluginBundle.message("ds008.workdir-path-not-absolute"),
                        ProblemHighlightType.WARNING
                    )
                }
            }
        }
    }
}