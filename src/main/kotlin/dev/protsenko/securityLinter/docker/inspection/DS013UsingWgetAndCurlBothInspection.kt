package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerPsiAnalyzer
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import java.util.concurrent.atomic.AtomicBoolean

class DS013UsingWgetAndCurlBothInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            //FIXME: Unnecessary atomic booleans
            val isContainCurl = AtomicBoolean()
            val isContainWget = AtomicBoolean()

            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                //FIXME: Allocating additional list of string
                val commandParts = DockerPsiAnalyzer.splitCommand(element)

                //FIXME: RUN curl wouldn't work
                if (commandParts.size <= 2) return

                //FIXME: Huge iterating!
                commandParts.forEach { command ->
                    when(command) {
                        "wget" -> isContainWget.set(true)
                        "curl" -> isContainCurl.set(true)
                    }
                }

                super.visitDockerFileRunCommand(element)
            }

            override fun visitingIsFinished(file: PsiFile) {
                if (isContainCurl.get() && isContainWget.get()){
                    holder.registerProblem(file, SecurityPluginBundle.message("ds013.standardise-remote-get"))
                }
            }
        }
    }
}