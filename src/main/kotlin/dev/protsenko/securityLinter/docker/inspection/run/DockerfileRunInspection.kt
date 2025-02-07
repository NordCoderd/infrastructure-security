package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.DockerPsiFile
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiElementVisitor.EMPTY_VISITOR
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer
import dev.protsenko.securityLinter.utils.toStringDockerCommand
import java.util.concurrent.atomic.AtomicBoolean

class DockerfileRunInspection : LocalInspectionTool() {
    val extensionPointName =
        ExtensionPointName.create<DockerfileRunAnalyzer>("dev.protsenko.security-linter.dockerFileRunAnalyzer")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (holder.file !is DockerPsiFile){
            return EMPTY_VISITOR
        }
        val extensions = extensionPointName.extensions

        return object : DockerfileVisitor() {
            val isContainCurl = AtomicBoolean()
            val isContainWget = AtomicBoolean()

            override fun visitDockerFileRunCommand(element: DockerFileRunCommand) {
                val execForm = element.parametersInJsonForm?.toStringDockerCommand("RUN ")
                val runCommand = if (execForm != null) {
                    execForm
                } else {
                    element.text
                }
                for (extension in extensions) {
                    extension.handle(runCommand, element, holder)
                }

                if ("wget" in runCommand) isContainWget.set(true)
                if ("curl" in runCommand) isContainCurl.set(true)
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