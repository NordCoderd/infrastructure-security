package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileExposeCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerPsiAnalyzer
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS007ExposedPortOutOfRangeInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileExposeCommand(element: DockerFileExposeCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size < 2 || commandParts.size > 3) return

                //TODO: Port definition also could be invalid. Should it be highlighted?
                val ports = commandParts.mapNotNull { commandPart ->
                    if (commandPart == "EXPOSE") return@mapNotNull null
                    try {
                        val normalizedPortNumber = commandPart.substringBefore("/")
                        Integer.parseInt(normalizedPortNumber)
                    } catch (_: NumberFormatException){
                        null
                    }
                }

                //TODO: Some ports could be exposed twice
                ports.forEach { port ->
                    if (port < 0 || port > 65535){
                        holder.registerProblem(element, SecurityPluginBundle.message("ds007.port-out-of-range", port.toString()))
                    }
                }

                super.visitDockerFileExposeCommand(element)
            }
        }
    }
}