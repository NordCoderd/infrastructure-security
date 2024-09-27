package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer
import java.util.concurrent.atomic.AtomicBoolean

class DS013UsingWgetAndCurlBothInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            val isContainCurl = AtomicBoolean()
            val isContainWget = AtomicBoolean()

            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                val command = element.text
                if (!command.contains("wget") && !command.contains("curl")) return

                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size <= 2) return

                commandParts.forEach { command ->
                    when(command) {
                        "wget" -> isContainWget.set(true)
                        "curl" -> isContainCurl.set(true)
                    }
                }
            }

            override fun visitingIsFinished(file: PsiFile) {
                if (isContainCurl.get() && isContainWget.get()){
                    holder.registerProblem(
                        file,
                        SecurityPluginBundle.message("ds013.standardise-remote-get"),
                        ProblemHighlightType.WARNING
                    )
                }
            }
        }
    }
}