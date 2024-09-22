package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS020MaintainerUsedInspectionTest(
    override val dockerRuleFolder: String = "DS020",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS020MaintainerUsedInspection()
) : DockerHighlightingBaseTest()