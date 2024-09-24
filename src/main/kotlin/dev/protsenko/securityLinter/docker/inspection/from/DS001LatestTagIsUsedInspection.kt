package dev.protsenko.securityLinter.docker.inspection.from

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.IntentionFamilyName
import com.intellij.docker.agent.DockerRepoTag
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.utils.DockerImageDigestFetcher
import dev.protsenko.securityLinter.utils.PsiElementGenerator

class DS001LatestTagIsUsedInspection : LocalInspectionTool() {

    companion object {
        const val ONLY_DOCKER_IMAGE_NAME = 1
        const val DOCKER_IMAGE_NAME_WITH_VERSION = 2
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                val children = element.nameChainList
                val imageName = children.firstOrNull()?.text ?: return
                when (children.size) {
                    ONLY_DOCKER_IMAGE_NAME -> {
                        holder.registerProblem(
                            element,
                            SecurityPluginBundle.message("ds001.missing-version-tag"),
                            ReplaceTagWithDigestQuickFix(imageName)
                        )
                    }

                    DOCKER_IMAGE_NAME_WITH_VERSION -> {
                        val tag = children.last()
                        if (tag.textMatches(DockerRepoTag.DEFAULT_TAG)) {
                            holder.registerProblem(
                                element,
                                SecurityPluginBundle.message("ds001.latest-tag"),
                                ReplaceTagWithDigestQuickFix(imageName)
                            )
                        }
                    }
                }
                return
            }
        }
    }

    private class ReplaceTagWithDigestQuickFix(private val imageName: String) : LocalQuickFix {
        override fun getFamilyName(): @IntentionFamilyName String =
            SecurityPluginBundle.message("ds001.lookup-for-digest")

        override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
            val fromToReplace = descriptor.psiElement

            try {
                ReadAction.run<Exception> {
                    val digest = DockerImageDigestFetcher.fetchDigest(imageName).get()
                    val dockerFileFromCommand = PsiElementGenerator.getDockerFileFromCommand(project, imageName, digest)
                        ?: return@run
                    fromToReplace.replace(dockerFileFromCommand)
                }
            } catch (e: Exception) {
                notifyError(project, "Failed to fetch digest for image '$imageName': ${e.message}")
            }
        }

        fun notifyError(project: Project, content: String) {
            NotificationGroupManager.getInstance()
                .getNotificationGroup("dev.protsenko.securityLinter")
                .createNotification(content, NotificationType.ERROR)
                .notify(project);
        }
    }
}