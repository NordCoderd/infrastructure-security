package dev.protsenko.securityLinter.docker_compose

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import dev.protsenko.securityLinter.utils.image.ImageAnalyzer
import dev.protsenko.securityLinter.utils.image.ImageDefinitionCreator
import org.jetbrains.yaml.psi.YAMLKeyValue

class DockerComposeInspection: LocalInspectionTool() {
    companion object {
        const val IMAGE_KEY_LITERAL = "image"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PsiElementVisitor(){
            override fun visitFile(file: PsiFile) {
                if (!file.name.startsWith("docker", ignoreCase = true)){
                    return
                }
                super.visitFile(file)
            }

            override fun visitElement(element: PsiElement) {
                if (element is YAMLKeyValue){
                    val keyValue = element.key?.text ?: return
                    when (keyValue){
                        // Analyzing image definition
                        IMAGE_KEY_LITERAL -> {
                            val imageName = element.value?.text ?: return
                            val imageDefinition = ImageDefinitionCreator.fromString(imageName, emptyMap())
                            ImageAnalyzer.analyzeAndHighlight(imageDefinition, holder, element)
                            super.visitElement(element)
                        }
                        else -> return
                    }
                }
            }
        }
    }
}