package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DS014YumInstallWithoutCleanInspection

class DS014YumInstallWithoutCleanInspectionTest(
    override val dockerRuleFolder: String = "DS014",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS014YumInstallWithoutCleanInspection()
) : DockerHighlightingBaseTest()