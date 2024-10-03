package dev.protsenko.securityLinter.core

import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileArgCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileCmdCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileEntrypointCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileEnvCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileExposeCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileHealthCheckCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileMaintainerCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileRunCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileUserCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileWorkdirCommand
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiFile
import com.jetbrains.rd.util.concurrentMapOf

open class DockerVisitor(private val saveArguments: Boolean = false) : PsiElementVisitor() {
    val resolvedVariables = concurrentMapOf<String, String>()
    var currentStep: String? = null

    override fun visitFile(file: PsiFile) {
        if (file.name == "Dockerfile") {
            super.visitFile(file)
            visitingIsFinished(file)
        }
    }

    override fun visitElement(element: PsiElement) {
        when (element) {
            is DockerFileFromCommand -> visitDockerFileFromCommand(element)
            is DockerFileUserCommand -> visitDockerFileUserCommand(element)
            is DockerFileExposeCommand -> visitDockerFileExposeCommand(element)
            is DockerFileAddOrCopyCommand -> visitDockerFileAddOrCopyCommand(element)
            is DockerFileEntrypointCommand -> visitDockerFileEntrypointCommand(element)
            is DockerFileWorkdirCommand -> visitDockerFileWorkdirCommand(element)
            is DockerFileRunCommand -> visitDockerFileRunCommand(element)
            is DockerFileCmdCommand -> visitDockerFileCmdCommand(element)
            is DockerFileMaintainerCommand -> visitDockerFileMaintainerCommand(element)
            is DockerFileHealthCheckCommand -> visitDockerFileHealthCheckCommand(element)
            is DockerFileArgCommand -> visitDockerFileArgCommand(element)
            is DockerFileEnvCommand -> visitDockerFileEnvCommand(element)
        }
        super.visitElement(element)
    }

    open fun visitDockerFileArgCommand(element: DockerFileArgCommand) {
        if (!saveArguments) return
        val argDeclaration = element.argDeclaration ?: return
        val key = argDeclaration.declaredName.text ?: return
        val value = argDeclaration.anyValue?.text ?: return
        resolvedVariables[key] = value
    }

    open fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
        val declaredStepName = element.fromStageDeclaration?.declaredName
        currentStep = declaredStepName?.text
    }

    open fun visitDockerFileUserCommand(element: DockerFileUserCommand) {}
    open fun visitDockerFileExposeCommand(element: DockerFileExposeCommand) {}
    open fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {}
    open fun visitDockerFileEntrypointCommand(element: DockerFileEntrypointCommand) {}
    open fun visitDockerFileWorkdirCommand(element: DockerFileWorkdirCommand) {}
    open fun visitDockerFileRunCommand(element: DockerFileRunCommand) {}
    open fun visitDockerFileCmdCommand(element: DockerFileCmdCommand) {}
    open fun visitDockerFileMaintainerCommand(element: DockerFileMaintainerCommand) {}
    open fun visitDockerFileHealthCheckCommand(element: DockerFileHealthCheckCommand) {}
    open fun visitDockerFileEnvCommand(element: DockerFileEnvCommand) {}

    open fun visitingIsFinished(file: PsiFile) {}
}