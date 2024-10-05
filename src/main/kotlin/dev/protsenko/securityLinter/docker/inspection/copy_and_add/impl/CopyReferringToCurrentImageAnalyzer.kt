package dev.protsenko.securityLinter.docker.inspection.copy_and_add.impl

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.inspection.copy_and_add.core.DockerfileCopyOrAddAnalyzer

class CopyReferringToCurrentImageAnalyzer : DockerfileCopyOrAddAnalyzer {
    companion object {
        const val COPY_FROM_OPTION_NAME = "from"
    }

    override fun handle(currentStep: String?, element: DockerFileAddOrCopyCommand, holder: ProblemsHolder) {
        if (element.addKeyword != null) return
        val copyFromOption = element.regularOptionList.filter { regularOption ->
            val optionName = regularOption.optionName ?: return@filter false
            optionName.text == COPY_FROM_OPTION_NAME
        }.firstOrNull() ?: return

        val step = copyFromOption.regularValue?.text ?: return

        if (step == currentStep) {
            holder.registerProblem(
                copyFromOption,
                SecurityPluginBundle.message("ds005.copy-referring-to-the-current-image"),
                ProblemHighlightType.ERROR,
            )
        }
    }
}