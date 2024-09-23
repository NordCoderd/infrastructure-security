package dev.protsenko.securityLinter.docker.inspection.copy

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS005CopyReferringToCurrentImageInspection: LocalInspectionTool() {
    companion object {
        const val COPY_FROM_OPTION_NAME = "from"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor(){
            var currentStep: String? = null

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                val declaredStepName = element.fromStageDeclaration?.declaredName
                currentStep = declaredStepName?.text
            }

            override fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {
                if (element.addKeyword!=null) return
                val copyFromOption = element.regularOptionList
                    .filter { regularOption ->
                        val optionName = regularOption.optionName ?: return@filter false
                        optionName.text == COPY_FROM_OPTION_NAME
                    }.firstOrNull() ?: return

                val step = copyFromOption.regularValue?.text ?: return

                if (step == currentStep){
                    holder.registerProblem(copyFromOption, SecurityPluginBundle.message("ds005.copy-referring-to-the-current-image"))
                }
            }
        }
    }
}