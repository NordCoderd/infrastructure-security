package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DockerfileRunInspection

class DS035UserAddMissingLFlag(
    override val ruleFolderName: String = "DS035",
    override val customFiles: Set<String> = setOf(
        "Dockerfile-user-add-without-uid.allowed"
    ),
    override val targetInspection: LocalInspectionTool = DockerfileRunInspection()
) : DockerHighlightingBaseTest()