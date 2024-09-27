package dev.protsenko.securityLinter.docker.inspection.from

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix

class DS011DuplicateAliasesDefinedInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            val aliases = mutableSetOf<String>()

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                val stageDeclaration = element.fromStageDeclaration ?: return
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