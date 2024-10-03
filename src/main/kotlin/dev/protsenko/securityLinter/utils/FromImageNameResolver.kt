package dev.protsenko.securityLinter.utils

import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand

object FromImageNameResolver {

    fun parseImageDefinition(fromCommand: DockerFileFromCommand, resolvedVariables: Map<String, String>): ImageDefinition? {
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
        val (rawImageName, rawVersion) = when (imageNameWithVersion.size) {
            1 -> imageNameWithVersion.first() to null
            2 -> imageNameWithVersion.first() to imageNameWithVersion.last()
            else -> return ImageDefinition(fullImageName, null)
        }

        val imageName = resolvedVariables[rawImageName.substringAfter("$")] ?: rawImageName
        val version = rawVersion?.let { resolvedVariables[it.substringAfter("$")] ?: it }

        return ImageDefinition(imageName, version)
    }

    data class ImageDefinition(
        val imageName: String,
        val version: String?
    )
}