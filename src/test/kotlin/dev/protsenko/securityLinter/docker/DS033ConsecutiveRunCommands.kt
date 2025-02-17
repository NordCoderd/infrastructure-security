package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DockerfileRunInspection

class DS033ConsecutiveRunCommands(
    override val ruleFolderName: String = "DS033",
    override val customFiles: Set<String> = setOf(
        "Dockerfile-three-commands.denied",
        "Dockerfile-user-command-between.allowed"
    ),
    override val targetInspection: LocalInspectionTool = DockerfileRunInspection()
) : DockerHighlightingBaseTest()