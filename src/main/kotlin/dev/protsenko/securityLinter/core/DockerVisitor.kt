package dev.protsenko.securityLinter.core

import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileCmdCommand
import com.intellij.docker.dockerFile.parser.psi.DockerFileEntrypointCommand
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

open class DockerVisitor : PsiElementVisitor() {

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
        }
        super.visitElement(element)
    }

    open fun visitDockerFileFromCommand(element: DockerFileFromCommand) {}
    open fun visitDockerFileUserCommand(element: DockerFileUserCommand) {}
    open fun visitDockerFileExposeCommand(element: DockerFileExposeCommand) {}
    open fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {}
    open fun visitDockerFileEntrypointCommand(element: DockerFileEntrypointCommand) {}
    open fun visitDockerFileWorkdirCommand(element: DockerFileWorkdirCommand){}
    open fun visitDockerFileRunCommand(element: DockerFileRunCommand){}
    open fun visitDockerFileCmdCommand(element: DockerFileCmdCommand){}
    open fun visitDockerFileMaintainerCommand(element: DockerFileMaintainerCommand){}
    open fun visitDockerFileHealthCheckCommand(element: DockerFileHealthCheckCommand){}
    open fun visitingIsFinished(file: PsiFile) {}
}