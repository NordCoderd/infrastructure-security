package dev.protsenko.securityLinter.utils.image

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.agent.DockerRepoTag
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle

object ImageAnalyzer {
    fun analyzeAndHighlight(imageDefinition: ImageDefinition, holder: ProblemsHolder, element: PsiElement){
        if (imageDefinition.version == null) {
            holder.registerProblem(
                element,
                SecurityPluginBundle.message("ds001.missing-version-tag"),
                ProblemHighlightType.ERROR,
                ReplaceTagWithDigestQuickFix(imageDefinition.imageName)
            )
        } else if (imageDefinition.version == DockerRepoTag.DEFAULT_TAG) {
            holder.registerProblem(
                element,
                SecurityPluginBundle.message("ds001.latest-tag"),
                ProblemHighlightType.ERROR,
                ReplaceTagWithDigestQuickFix(imageDefinition.imageName),
            )
        }
    }
}