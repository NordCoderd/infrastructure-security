package dev.protsenko.securityLinter.utils

import com.intellij.docker.dockerFile.DockerFileType
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.openapi.fileTypes.PlainTextFileType
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.childrenOfType

object PsiElementGenerator {
    const val DUMMY_FILE_NAME = "Dockerfile"

    inline fun <reified T : PsiElement> fromText(project: Project, text: String): T? {
        val fullDockerFileText = "FROM dummy\n$text"
        val factory = PsiFileFactory.getInstance(project)
        val dockerFile = factory.createFileFromText(DUMMY_FILE_NAME, DockerFileType.DOCKER_FILE_TYPE, fullDockerFileText)
        return dockerFile.childrenOfType<T>().firstOrNull()
    }

    fun getDockerFileFromCommand(project: Project, image: String, digest: String): DockerFileFromCommand? {
        val factory = PsiFileFactory.getInstance(project)
        val dockerFile = factory.createFileFromText(
            DUMMY_FILE_NAME,
            DockerFileType.DOCKER_FILE_TYPE,
            "FROM $image@$digest"
        )
        return dockerFile.childrenOfType<DockerFileFromCommand>().firstOrNull()
    }

    fun newLine(project: Project): PsiElement? {
        val factory = PsiFileFactory.getInstance(project)
        val emptyFile = factory.createFileFromText("raw", PlainTextFileType.INSTANCE, "\n")
        return emptyFile.firstChild
    }

}