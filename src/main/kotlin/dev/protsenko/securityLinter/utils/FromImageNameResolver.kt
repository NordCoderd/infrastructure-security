package dev.protsenko.securityLinter.utils

import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand

object FromImageNameResolver {

    fun parseImageDefinition(fromCommand: DockerFileFromCommand): ImageDefinition? {
        val fromCommandText = fromCommand.text
        val nameChainList = fromCommand.nameChainList.map { it.text }
        if (nameChainList.isEmpty()) return null

        // Looking for the first and last element of name
        val firstElement = nameChainList.first()
        val lastElement = nameChainList.last()
        if (firstElement == lastElement) return ImageDefinition(firstElement, null)

        // Looking for positions in the original line
        val fistPosition = fromCommandText.indexOf(firstElement)
        val lastPosition = fromCommandText.indexOf(lastElement) + lastElement.length

        // Retrieving image name with version
        val fullImageName = fromCommandText.substring(fistPosition, lastPosition)
        val splitSymbol = if ("@" in fullImageName) "@" else ":"

        val imageNameWithVersion = fullImageName.split(splitSymbol)
        if (imageNameWithVersion.size == 1) return ImageDefinition(imageNameWithVersion.first(), null)
        if (imageNameWithVersion.size == 2) return ImageDefinition(imageNameWithVersion.first(), imageNameWithVersion.last())

        return ImageDefinition(fullImageName, null)
    }

    data class ImageDefinition(
        val imageName: String,
        val version: String?
    )
}