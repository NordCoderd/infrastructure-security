package dev.protsenko.securityLinter.utils.image

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.PsiElementGenerator
import org.jetbrains.yaml.psi.YAMLKeyValue

class ReplaceTagWithDigestQuickFix(private val imageName: String) : LocalQuickFix {

    override fun getFamilyName(): @IntentionFamilyName String =
        SecurityPluginBundle.message("ds001.lookup-for-digest")

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        DockerImageDigestFetcher.fetchDigest(imageName)
            .thenAccept { digest ->
                ApplicationManager.getApplication().invokeLater {
                    WriteCommandAction.runWriteCommandAction(project) {
                        replaceImageName(project, descriptor, digest)
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

    private fun replaceImageName(project: Project, descriptor: ProblemDescriptor, digest: String){
        val targetElement = descriptor.psiElement
        if (targetElement is DockerFileFromCommand){
            val dockerFileFromCommand =
                PsiElementGenerator.getDockerFileFromCommand(project, imageName, digest) ?: return
            targetElement.replace(dockerFileFromCommand)
        } else if (targetElement is YAMLKeyValue){
            val imageDefinitionWithDigest =
                PsiElementGenerator.rawText(project, "$imageName@$digest") ?: return
            val targetValueElement = targetElement.value ?: return
            targetValueElement.replace(imageDefinitionWithDigest)
        }
    }

    fun notifyError(project: Project, content: String) {
        NotificationGroupManager.getInstance()
            .getNotificationGroup("dev.protsenko.securityLinter")
            .createNotification(content, NotificationType.ERROR)
            .notify(project)
    }
}