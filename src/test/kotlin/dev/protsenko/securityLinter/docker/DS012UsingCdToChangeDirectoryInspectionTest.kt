package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.DS012UsingCdToChangeDirectoryInspection

class DS012UsingCdToChangeDirectoryInspectionTest(
    override val dockerRuleFolder: String = "DS012",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS012UsingCdToChangeDirectoryInspection()
) : DockerHighlightingBaseTest()