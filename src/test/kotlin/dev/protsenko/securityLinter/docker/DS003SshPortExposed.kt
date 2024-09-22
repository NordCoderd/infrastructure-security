package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS002SshPortExposed(
    override val dockerRuleFolder: String = "DS003",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS003SshPortExposedInspection()
) : DockerHighlightingBaseTest()