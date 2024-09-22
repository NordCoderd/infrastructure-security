package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileEntrypointCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import java.util.concurrent.atomic.AtomicInteger

class DS006MultipleEntrypointInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            val entryPointCount = AtomicInteger(0)

            override fun visitDockerFileEntrypointCommand(element: DockerFileEntrypointCommand) {
                entryPointCount.incrementAndGet()
            }

            override fun visitingIsFinished(file: PsiFile) {
                if (entryPointCount.get() > 1){
                    holder.registerProblem(file, SecurityPluginBundle.message("ds006.multiple-entrypoint"))
                }
            }

        }
    }
}