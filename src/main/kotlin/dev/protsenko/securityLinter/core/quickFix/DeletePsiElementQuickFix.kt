package dev.protsenko.securityLinter.core.quickFix

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.openapi.project.Project
import dev.protsenko.securityLinter.utils.modifyPsi

class DeletePsiElementQuickFix(@IntentionFamilyName private val message: String) : LocalQuickFix {
    override fun getFamilyName(): @IntentionFamilyName String = this.message

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        modifyPsi(project) {
            descriptor.psiElement.delete()
        }
    }
}