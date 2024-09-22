package dev.protsenko.securityLinter.docker.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS011DuplicateAliasesDefinedInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            val aliases = mutableSetOf<String>()

            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                val declaredStepName = element.fromStageDeclaration?.declaredName ?: return
                val declaredStep = declaredStepName.text
                if (aliases.contains(declaredStep)){
                    holder.registerProblem(declaredStepName, SecurityPluginBundle.message("ds011.no-duplicate-alias"))
                } else {
                    aliases.add(declaredStep)
                }
            }
        }
    }
}