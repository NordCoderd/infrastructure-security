package dev.protsenko.securityLinter.docker.inspection.from

import com.intellij.codeInspection.LocalInspectionTool
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
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.DockerImageDigestFetcher
import dev.protsenko.securityLinter.utils.FromImageNameResolver
import dev.protsenko.securityLinter.utils.PsiElementGenerator

class DS001LatestTagIsUsedInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                val imageDefinition = FromImageNameResolver.parseImageDefinition(element) ?: return
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
        }
    }

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
}