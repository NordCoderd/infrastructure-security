package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS004AddInsteadCopyInspectionTest(
    override val dockerRuleFolder: String = "DS004",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS004AddInsteadCopyInspection()
) : DockerHighlightingBaseTest()