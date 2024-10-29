package dev.protsenko.securityLinter.docker_compose

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.utils.image.ImageAnalyzer
import dev.protsenko.securityLinter.utils.image.ImageDefinitionCreator
import dev.protsenko.securityLinter.utils.isChildOfServiceDefinition
import org.jetbrains.yaml.psi.YAMLKeyValue

class DockerComposeInspection: LocalInspectionTool() {
    companion object {
        const val IMAGE_KEY_LITERAL = "image"
        val supportedAttributes = setOf(IMAGE_KEY_LITERAL)
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor(){
            override fun visitFile(file: PsiFile) {
                if (!file.name.startsWith("docker", ignoreCase = true)){
                    return
                }
                super.visitFile(file)
            }

            /**
             * For more information about service attributes: https://docs.docker.com/reference/compose-file/services
             */
            override fun visitElement(element: PsiElement) {
                if (element is YAMLKeyValue){
                    val attributeName = element.key?.text ?: return
                    val attributeValue = element.value?.text ?: return
                    if (attributeName !in supportedAttributes) return
                    if (!element.isChildOfServiceDefinition()) return

                    when (attributeName){
                        // Analyzing image definition
                        IMAGE_KEY_LITERAL -> {
                            val imageDefinition = ImageDefinitionCreator.fromString(attributeValue, emptyMap())
                            ImageAnalyzer.analyzeAndHighlight(imageDefinition, holder, element)
                        }
                        else -> return
                    }
                }
            }
        }
    }
}