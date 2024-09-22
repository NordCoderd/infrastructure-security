package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS021MultipleHealthCheckDefinedInspectionTest(
    override val dockerRuleFolder: String = "DS021",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS021MultipleHealthCheckDefinedInspection()
) : DockerHighlightingBaseTest()