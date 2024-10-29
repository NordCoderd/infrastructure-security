package dev.protsenko.securityLinter.utils

import org.jetbrains.yaml.psi.YAMLKeyValue

/**
 * Used dirty trick to avoiding loops
 */
fun YAMLKeyValue.isChildOfServiceDefinition(): Boolean {
    var lastPsiElement = this.parent?.parent?.parent?.parent ?: return false
    return lastPsiElement is YAMLKeyValue && lastPsiElement.keyText == "services"
}

