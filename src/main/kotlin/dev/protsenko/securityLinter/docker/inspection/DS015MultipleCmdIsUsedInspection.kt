package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileCmdCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import java.util.concurrent.atomic.AtomicInteger

class DS015MultipleCmdIsUsedInspection: LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object: DockerVisitor(){
            val cmdCount = AtomicInteger(0)

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                cmdCount.set(0)
            }

            override fun visitDockerFileCmdCommand(element: DockerFileCmdCommand) {
                if (cmdCount.incrementAndGet() > 1){
                    holder.registerProblem(element, SecurityPluginBundle.message("ds015.only-one-cmd"))
                }
            }
        }
    }


}