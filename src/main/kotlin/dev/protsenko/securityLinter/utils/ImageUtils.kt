package dev.protsenko.securityLinter.utils

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.agent.DockerRepoTag
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import dev.protsenko.securityLinter.core.SecurityPluginBundle

object ImageUtils {

    fun parseImageDefinition(fromCommand: DockerFileFromCommand): ImageDefinition? {
        val fromCommandText = fromCommand.text
        val nameChainList = fromCommand.nameChainList.map { it.text }
        val resolvedVariables = fromCommand.nameChainList
            .flatMap { name ->
                name.variableRefSimpleList.mapNotNull {
                    variableReference ->
                    val variableReferenceName = variableReference.referencedName?.text ?: return@mapNotNull null
                    val resolvedVariable = variableReference.resolveVariable() ?: return@mapNotNull null
                    variableReferenceName to resolvedVariable
                }
            }
            .toMap()

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
        return createImageDefinitionFromString(fullImageName, resolvedVariables)
    }

    fun createImageDefinitionFromString(fullImageName: String,
                                                resolvedVariables: Map<String, String>): ImageDefinition {
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

    fun highlightImageDefinitionProblem(imageDefinition: ImageDefinition, holder: ProblemsHolder, element: PsiElement){
        if (imageDefinition.version == null) {
            holder.registerProblem(
                element,
                SecurityPluginBundle.message("ds001.missing-version-tag"),
                ProblemHighlightType.ERROR,
                ReplaceTagWithDigestQuickFix(imageDefinition.imageName)
            )
        } else if (imageDefinition.version == DockerRepoTag.DEFAULT_TAG) {
            holder.registerProblem(
                element,
                SecurityPluginBundle.message("ds001.latest-tag"),
                ProblemHighlightType.ERROR,
                ReplaceTagWithDigestQuickFix(imageDefinition.imageName),
            )
        }
    }

    //FIXME: rewrite to support yaml values
    private class ReplaceTagWithDigestQuickFix(private val imageName: String) : LocalQuickFix {
        override fun getFamilyName(): @IntentionFamilyName String =
            SecurityPluginBundle.message("ds001.lookup-for-digest")

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val fromToReplace = descriptor.psiElement

            DockerImageDigestFetcher.fetchDigest(imageName)
                .thenAccept { digest ->
                    ApplicationManager.getApplication().invokeLater {
                        WriteCommandAction.runWriteCommandAction(project) {
                            val dockerFileFromCommand =
                                PsiElementGenerator.getDockerFileFromCommand(project, imageName, digest)
                                    ?: return@runWriteCommandAction
                            fromToReplace.replace(dockerFileFromCommand)
                        }
                    }
                }
                .exceptionally { throwable ->
                    ApplicationManager.getApplication().invokeLater {
                        notifyError(project, "Failed to fetch digest for image '$imageName': ${throwable.message}")
                    }
                    null
                }
        }

        fun notifyError(project: Project, content: String) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("dev.protsenko.securityLinter")
                .createNotification(content, NotificationType.ERROR)
                .notify(project)
        }
    }

    data class ImageDefinition(
        val imageName: String,
        val version: String?
    )
}