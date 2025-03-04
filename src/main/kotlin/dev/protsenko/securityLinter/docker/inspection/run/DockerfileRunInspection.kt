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
import dev.protsenko.securityLinter.core.HtmlProblemDescriptor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.inspection.run.core.DockerfileRunAnalyzer
import dev.protsenko.securityLinter.utils.toStringDockerCommand

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

                val curlCommands = mutableListOf<DockerFileRunCommand>()
                val wgetCommands = mutableListOf<DockerFileRunCommand>()

                val runCommands = file.findChildrenByClass(DockerFileRunCommand::class.java)

                var currentOffset = 0
                val runCommandsToHighlight = mutableListOf<DockerFileRunCommand>()
                runCommands.forEach { element ->
                    val execForm = element.parametersInJsonForm?.toStringDockerCommand("RUN ")
                    val runCommand = execForm ?: element.text
                    for (extension in extensions) {
                        extension.handle(runCommand, element, holder)
                    }

                    if ("wget" in runCommand) wgetCommands.add(element)
                    if ("curl" in runCommand) curlCommands.add(element)

                    val textRange = element.textRange
                    if (currentOffset == 0){
                        currentOffset = textRange.endOffset
                    } else {
                        if (currentOffset + 1 != textRange.startOffset) {
                            runCommandsToHighlight.clear()
                        }
                        currentOffset = textRange.endOffset
                    }
                    runCommandsToHighlight.add(element)
                }

                if (runCommandsToHighlight.size > 1){
                    runCommandsToHighlight.forEach { runCommand ->
                        val descriptor = HtmlProblemDescriptor(
                            runCommand,
                            SecurityPluginBundle.message("dfs028.documentation"),
                            SecurityPluginBundle.message("dfs028.multiple-consecutive-run-commands"),
                            ProblemHighlightType.WARNING
                        )

                        holder.registerProblem(descriptor)
                    }
                }

                if (curlCommands.isNotEmpty() && wgetCommands.isNotEmpty()){
                    (curlCommands + wgetCommands).forEach { runCommand ->
                        val descriptor = HtmlProblemDescriptor(
                            runCommand,
                            SecurityPluginBundle.message("dfs026.documentation"),
                            SecurityPluginBundle.message("dfs026.standardise-remote-get"),
                            ProblemHighlightType.WARNING
                        )

                        holder.registerProblem(descriptor)
                    }
                }
            }
        }
    }
}