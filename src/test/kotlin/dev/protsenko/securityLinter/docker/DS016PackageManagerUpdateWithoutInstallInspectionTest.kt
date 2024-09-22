package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS015MultipleCmdIsUsedInspectionTest(
    override val dockerRuleFolder: String = "DS015",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS015MultipleCmdIsUsedInspection()
) : DockerHighlightingBaseTest()