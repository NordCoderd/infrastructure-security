package dev.protsenko.securityLinter.docker.inspection.from

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.DockerPsiFile
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileVisitor
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiElementVisitor.EMPTY_VISITOR
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import dev.protsenko.securityLinter.utils.image.ImageAnalyzer
import dev.protsenko.securityLinter.utils.image.ImageDefinition
import dev.protsenko.securityLinter.utils.image.ImageDefinitionCreator

class DockerfileFromInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        if (holder.file !is DockerPsiFile) return EMPTY_VISITOR

        return object : DockerFileVisitor() {
            override fun visitFile(file: PsiFile) {
                if (file !is DockerPsiFile) return

                val fromCommands = file.findChildrenByClass(DockerFileFromCommand::class.java)
                val aliasToImageDefinition = mutableMapOf<String, ImageDefinition>()

                fromCommands.forEach { fromCommand ->
                    val imageDefinition = ImageDefinitionCreator.fromDockerFileFromCommand(fromCommand) ?: return
                    ImageAnalyzer.analyzeAndHighlight(imageDefinition, holder, fromCommand, aliasToImageDefinition)

                    val stageDeclaration = fromCommand.fromStageDeclaration ?: return@forEach
                    val declaredStepName = stageDeclaration.declaredName
                    val declaredStep = declaredStepName.text

                    if (aliasToImageDefinition.containsKey(declaredStep)) {
                        holder.registerProblem(
                            stageDeclaration,
                            SecurityPluginBundle.message("ds011.no-duplicate-alias"),
                            ProblemHighlightType.WARNING,
                            DeletePsiElementQuickFix(SecurityPluginBundle.message("ds011.remove-duplicated-alias"))
                        )
                    } else {
                        aliasToImageDefinition.put(declaredStep, imageDefinition)
                    }
                }
            }
        }
    }
}