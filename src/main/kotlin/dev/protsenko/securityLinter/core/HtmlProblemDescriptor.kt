package dev.protsenko.securityLinter.core

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ex.ProblemDescriptorImpl
import com.intellij.openapi.util.NlsContexts
import com.intellij.psi.PsiElement

class HtmlProblemDescriptor(element: PsiElement,
                            descriptionTemplate: String,
                            private val toolTip: String? = null,
                            highlightType: ProblemHighlightType,
                            fixes: Array<LocalQuickFix> = emptyArray()
) :
    ProblemDescriptorImpl(
        element,
        element,
        descriptionTemplate,
        fixes,
        highlightType,
        false,
        null,
        true,
        null,
        true
    ) {
    override fun showTooltip() = toolTip != null

    override fun getTooltipTemplate(): @NlsContexts.Tooltip String {
        return toolTip ?: descriptionTemplate
    }
}