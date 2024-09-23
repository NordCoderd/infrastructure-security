package dev.protsenko.securityLinter.docker.inspection.entrypoint

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.parser.psi.DockerFileEntrypointCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import java.util.concurrent.atomic.AtomicInteger

class DS006MultipleEntrypointInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            val entryPointCount = AtomicInteger(0)
            val entryPoints = mutableListOf<DockerFileEntrypointCommand>()

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                detectProblemAndDropCounters()
            }

            override fun visitDockerFileEntrypointCommand(element: DockerFileEntrypointCommand) {
                entryPointCount.incrementAndGet()
                entryPoints.add(element)
            }

            override fun visitingIsFinished(file: PsiFile) {
                detectProblemAndDropCounters()
            }

            private fun detectProblemAndDropCounters() {
                if (entryPoints.size > 1) {
                    for (command in entryPoints.dropLast(1)) {
                        holder.registerProblem(
                            command,
                            SecurityPluginBundle.message("ds006.multiple-entrypoint"),
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds006.remove-redundant-entrypoint"))
                        )
                    }
                }

                entryPointCount.set(0)
                entryPoints.clear()
            }
        }
    }

}