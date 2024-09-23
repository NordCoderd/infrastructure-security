package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DS017MissingDnfCleanInspection

class DS017MissingDnfCleanInspectionTest(
    override val dockerRuleFolder: String = "DS017",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS017MissingDnfCleanInspection()
) : DockerHighlightingBaseTest()