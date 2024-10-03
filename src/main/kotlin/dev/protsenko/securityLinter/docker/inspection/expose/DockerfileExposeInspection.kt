package dev.protsenko.securityLinter.docker.inspection.expose

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileExposeCommand
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.docker.inspection.expose.core.DockerfileExposeAnalyzer
import dev.protsenko.securityLinter.utils.DockerPsiAnalyzer

class DockerfileExposeInspection: LocalInspectionTool() {
    val extensionPointName =
        ExtensionPointName.create<DockerfileExposeAnalyzer>("dev.protsenko.security-linter.dockerFileExposeAnalyzer")

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            val extensions = extensionPointName.extensions

            override fun visitDockerFileExposeCommand(element: DockerFileExposeCommand) {
                val commandParts = DockerPsiAnalyzer.splitCommand(element)
                if (commandParts.size < 2) return

                val ports = commandParts.mapNotNull { commandPart ->
                    try {
                        val normalizedPortNumber = commandPart.substringBefore("/")
                        Integer.parseInt(normalizedPortNumber)
                    } catch (_: NumberFormatException){
                        null
                    }
                }

                for (extension in extensions) {
                    extension.handle(ports, element, holder)
                }
            }
        }
    }
}