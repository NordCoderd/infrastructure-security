package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DS009SudoIsUsedInspection

class DS009SudoIsUsedInspectionTest(
    override val dockerRuleFolder: String = "DS009",
    override val customDockerFiles: Set<String> = emptySet(),
    override val targetInspection: LocalInspectionTool = DS009SudoIsUsedInspection()
) : DockerHighlightingBaseTest()