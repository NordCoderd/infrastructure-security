package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.expose.DockerfileExposeInspection

class DS003SshPortExposed(
    override val dockerRuleFolder: String = "DS003",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DockerfileExposeInspection()
) : DockerHighlightingBaseTest()