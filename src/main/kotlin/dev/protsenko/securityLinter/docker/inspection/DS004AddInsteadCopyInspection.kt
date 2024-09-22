package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.docker.dockerFile.parser.psi.DockerFileAddOrCopyCommand
import com.intellij.psi.PsiElementVisitor
import dev.protsenko.securityLinter.core.DockerFileInspectionConstants.FILE_EXTENSIONS_FOR_ADD_COMMAND
import dev.protsenko.securityLinter.core.DockerVisitor
import dev.protsenko.securityLinter.core.SecurityPluginBundle
import kotlin.io.path.Path
import kotlin.io.path.extension

class DS004AddInsteadCopyInspection: LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object: DockerVisitor(){
            override fun visitDockerFileAddOrCopyCommand(element: DockerFileAddOrCopyCommand) {
                if (element.copyKeyword != null) return

                val filesFromJson = element.parametersInJsonForm?.children?.map { it.text.removeQuotes() } ?: emptyList()
                val filesFromList = element.fileOrUrlList.map { it.text.removeQuotes() }

                val files = if (filesFromJson.isNotEmpty() && filesFromList.size >= 2){
                    filesFromJson.subList(0, filesFromList.size - 2)
                } else if (filesFromList.isNotEmpty() && filesFromList.size >= 2){
                    filesFromList.subList(0, filesFromList.size - 2)
                } else {
                    null
                } ?: return

                for (file in files) {
                    val pathToFile = Path(file)
                    if (!FILE_EXTENSIONS_FOR_ADD_COMMAND.contains(pathToFile.extension)){
                        holder.registerProblem(element, SecurityPluginBundle.message("ds004.add-instead-copy"))
                        return
                    }
                }
            }
        }
    }
}