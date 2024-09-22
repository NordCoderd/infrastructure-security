package dev.protsenko.securityLinter.docker.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileHealthCheckCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import java.util.concurrent.atomic.AtomicInteger

class DS021MultipleHealthCheckDefinedInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            val healthCheckCount = AtomicInteger(0)

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                healthCheckCount.set(0)
            }

            override fun visitDockerFileHealthCheckCommand(element: DockerFileHealthCheckCommand) {
                if (healthCheckCount.incrementAndGet() > 1){
                    holder.registerProblem(element, SecurityPluginBundle.message("ds021.only-one-healthcheck"))
                }
            }
        }
    }
}