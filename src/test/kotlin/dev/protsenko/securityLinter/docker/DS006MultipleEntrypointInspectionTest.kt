package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.entrypoint.DS006MultipleEntrypointInspection

class DS006MultipleEntrypointInspectionTest(
    override val dockerRuleFolder: String = "DS006",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS006MultipleEntrypointInspection()
) : DockerHighlightingBaseTest()