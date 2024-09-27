package dev.protsenko.securityLinter.docker.inspection.expose

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileExposeCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer

class DS007ExposedPortOutOfRangeInspection : LocalInspectionTool() {
    companion object {
        const val EXPOSE_COMMAND = "EXPOSE"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileExposeCommand(element: DockerFileExposeCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size < 2 || commandParts.size > 3) return

                //TODO: DS024 Port definition also could be invalid. Should it be highlighted?
                val ports = commandParts.mapNotNull { commandPart ->
                    if (commandPart == EXPOSE_COMMAND) return@mapNotNull null
                    try {
                        val normalizedPortNumber = commandPart.substringBefore("/")
                        Integer.parseInt(normalizedPortNumber)
                    } catch (_: NumberFormatException){
                        null
                    }
                }

                //TODO: DS025 Some ports could be exposed twice
                ports.forEach { port ->
                    if (port < 0 || port > 65535){
                        holder.registerProblem(
                            element,
                            SecurityPluginBundle.message("ds007.port-out-of-range", port.toString()),
                            ProblemHighlightType.ERROR,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds007.remove-broken-port"))
                        )
                    }
                }

                super.visitDockerFileExposeCommand(element)
            }
        }
    }
}