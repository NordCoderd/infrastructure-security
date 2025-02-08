package dev.protsenko.securityLinter.docker.inspection.run

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.DockerPsiFile
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileVisitor
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiElementVisitor.EMPTY_VISITOR
import com.intellij.psi.PsiFile
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

        return object : DockerFileVisitor() {
            override fun visitFile(file: PsiFile) {
                if (file !is DockerPsiFile){
                    return
                }

                val isContainCurl = AtomicBoolean()
                val isContainWget = AtomicBoolean()
                val runCommands = file.findChildrenByClass(DockerFileRunCommand::class.java)
                runCommands.forEach { element ->
                    val execForm = element.parametersInJsonForm?.toStringDockerCommand("RUN ")
                    val runCommand = execForm ?: element.text
                    for (extension in extensions) {
                        extension.handle(runCommand, element, holder)
                    }

                    if ("wget" in runCommand) isContainWget.set(true)
                    if ("curl" in runCommand) isContainCurl.set(true)
                }

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