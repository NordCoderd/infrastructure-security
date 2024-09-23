package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.cmd.DS015MultipleCmdIsUsedInspection

class DS015MultipleCmdIsUsedInspectionTest(
    override val dockerRuleFolder: String = "DS015",
    override val customDockerFiles: Set<String> = setOf("Dockerfile-multiple.denied"),
    override val targetInspection: LocalInspectionTool = DS015MultipleCmdIsUsedInspection()
) : DockerHighlightingBaseTest()