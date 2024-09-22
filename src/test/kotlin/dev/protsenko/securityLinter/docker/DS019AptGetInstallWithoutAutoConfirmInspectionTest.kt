package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS018ZypperInstallWithoutCleanInspectionTest(
    override val dockerRuleFolder: String = "DS018",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS018ZypperInstallWithoutCleanInspection()
) : DockerHighlightingBaseTest()