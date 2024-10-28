package dev.protsenko.securityLinter.docker_compose

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.utils.ImageUtils
import org.jetbrains.yaml.psi.YAMLKeyValue

class DockerComposeInspection: LocalInspectionTool() {
    companion object {
        const val IMAGE_KEY_LITERAL = "image"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor(){
            override fun visitElement(element: PsiElement) {
                if (element is YAMLKeyValue){
                    val keyName = element.key?.text ?: return
                    if (keyName == IMAGE_KEY_LITERAL){
                        val imageName = element.value?.text ?: return
                        val imageDefinition = ImageUtils.createImageDefinitionFromString(imageName, emptyMap())
                        ImageUtils.highlightImageDefinitionProblem(imageDefinition, holder, element)
                        println("${element.javaClass} - ${element.text}")
                        super.visitElement(element)
                    }
                }
            }
        }
    }
}