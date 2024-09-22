package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS006MultipleEntrypointInspectionTest(
    override val dockerRuleFolder: String = "DS006",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS006MultipleEntrypointInspection()
) : DockerHighlightingBaseTest()