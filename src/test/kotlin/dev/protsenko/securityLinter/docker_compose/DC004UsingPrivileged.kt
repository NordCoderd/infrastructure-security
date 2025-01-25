package dev.protsenko.securityLinter.docker_compose

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerComposeHighlightingBaseTest

class DC004UsingPrivileged(
    override val ruleFolderName: String = "DC004",
    override val targetInspection: LocalInspectionTool = DockerComposeInspection(),
    override val customFiles: Set<String> = emptySet<String>()
): DockerComposeHighlightingBaseTest()