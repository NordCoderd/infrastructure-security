package dev.protsenko.securityLinter.docker.inspection.env

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileEnvCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerfileVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.core.quickFix.DeletePsiElementQuickFix
import dev.protsenko.securityLinter.utils.DockerfileConstants.POTENTIAL_SECRETS_NAME

class DockerFileEnvInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerfileVisitor() {
            override fun visitDockerFileEnvCommand(element: DockerFileEnvCommand) {
                element.envRegularDeclarationList.forEach {
                    val declaredName = it.declaredName.text.uppercase()
                    if (!POTENTIAL_SECRETS_NAME.contains(declaredName)) return@forEach
                    holder.registerProblem(
                        it,
                        SecurityPluginBundle.message("ds026.possible-secrets-in-env", declaredName),
                        ProblemHighlightType.ERROR,
                        DeletePsiElementQuickFix(SecurityPluginBundle.message("ds026.remove-env-with-secret"))
                    )
                }
            }
        }
    }
}