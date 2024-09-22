package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileFromCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle

class DS001LatestTagIsUsedInspection : LocalInspectionTool() {

    companion object {
        const val ONLY_DOCKER_IMAGE_NAME = 1
        const val DOCKER_IMAGE_NAME_WITH_VERSION = 2
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : DockerVisitor() {
            override fun visitDockerFileFromCommand(element: DockerFileFromCommand) {
                val children = element.nameChainList
                when (children.size){
                    ONLY_DOCKER_IMAGE_NAME -> {
                        holder.registerProblem(element, SecurityPluginBundle.message("ds001.missing-version-tag"))
                    }
                    DOCKER_IMAGE_NAME_WITH_VERSION -> {
                        val tag = children.last()
                        if (tag.textMatches("latest")){
                            holder.registerProblem(element, SecurityPluginBundle.message("ds001.latest-tag"))
                        }
                    }
                }
                return
            }
        }
    }
}