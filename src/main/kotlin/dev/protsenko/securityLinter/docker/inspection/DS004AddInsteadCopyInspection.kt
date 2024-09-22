package dev.protsenko.securityLinter.docker.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import dev.protsenko.securityLinter.docker.extension
import dev.protsenko.securityLinter.docker.removeQuotes

class DS004AddInsteadCopyInspection: LocalInspectionTool() {
    companion object {
        const val GZ_FILE_EXTENSION = "gz"
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object: DockerVisitor(){
            override fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {
                if (element.copyKeyword != null) return

                val filesFromJson = element.parametersInJsonForm?.children?.map { it.text.removeQuotes() } ?: emptyList()
                val filesFromList = element.fileOrUrlList.map { it.text.removeQuotes() }

                val files = if (filesFromJson.isNotEmpty() && filesFromJson.size >= 2){
                    filesFromJson.subList(0, filesFromJson.size - 1)
                } else if (filesFromList.isNotEmpty() && filesFromList.size >= 2){
                    filesFromList.subList(0, filesFromList.size - 1)
                } else {
                    null
                } ?: return

                for (fileName in files) {
                    if (fileName.extension() != GZ_FILE_EXTENSION){
                        holder.registerProblem(element, SecurityPluginBundle.message("ds004.add-instead-copy"))
                        return
                    }
                }
            }
        }
    }
}