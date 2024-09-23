package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DS022DistUpgradeInspection

class DS022DistUpgradeInspectionTest(
    override val dockerRuleFolder: String = "DS022",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS022DistUpgradeInspection()
) : DockerHighlightingBaseTest()