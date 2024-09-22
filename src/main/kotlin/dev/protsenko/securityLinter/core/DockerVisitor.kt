package org.jetbrains.plugins.template.core

import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor

open class DockerVisitor: PsiElementVisitor() {

    open fun visitDockerFileFromCommand(element: DockerFileFromCommand){}

    override fun visitElement(element: PsiElement) {
        if (element is DockerFileFromCommand){
            visitDockerFileFromCommand(element)
            return
        }

        super.visitElement(element)
    }

}