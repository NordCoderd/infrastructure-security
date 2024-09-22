package dev.protsenko.securityLinter.core

import com.intellij.docker.dockerFile.parser.psi.DockerPsiCommand

object DockerPsiAnalyzer {

    /**
     * By some reasons Docker plugin in some cases return PSI elements without children.
     * In that case we should retrieve value of docker command.
     * ex: EXPOSE 22 -> 22; USER root -> root
     * TODO: cache values by the element
     * returns: value in lowercase
     */
    fun splitCommand(dockerElement: DockerPsiCommand): List<String> {
        return dockerElement
            .text
            .trim()
            .split(" ")
            .map { it.trim() }
    }

}