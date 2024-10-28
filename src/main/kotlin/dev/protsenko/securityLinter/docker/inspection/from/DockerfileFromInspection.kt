package dev.protsenko.securityLinter.docker.inspection.from

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import dev.protsenko.securityLinter.utils.image.ImageAnalyzer
import dev.protsenko.securityLinter.utils.image.ImageDefinitionCreator

class DockerfileFromInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerfileVisitor(trackStages = true) {
            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                super.visitDockerFileFromCommand(element)
                val imageDefinition = ImageDefinitionCreator.fromDockerFileFromCommand(element) ?: return
                ImageAnalyzer.analyzeAndHighlight(imageDefinition, holder, element)
            }

            override fun visitingIsFinished(file: PsiFile) {
                val aliases = mutableSetOf<String>()
                buildStages.forEach { (_, fromCommand) ->
                    val stageDeclaration = fromCommand.fromStageDeclaration ?: return@forEach
                    val declaredStepName = stageDeclaration.declaredName
                    val declaredStep = declaredStepName.text
                    if (aliases.contains(declaredStep)){
                        holder.registerProblem(
                            stageDeclaration,
                            SecurityPluginBundle.message("ds011.no-duplicate-alias"),
                            ProblemHighlightType.WARNING,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds011.remove-duplicated-alias"))
                        )
                    } else {
                        aliases.add(declaredStep)
                    }
                }
            }
        }
    }
}