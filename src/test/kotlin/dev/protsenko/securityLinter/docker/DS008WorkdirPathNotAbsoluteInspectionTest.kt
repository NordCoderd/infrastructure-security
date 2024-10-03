package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.workdir.DockerfileWorkdirInspection

class DS008WorkdirPathNotAbsoluteInspectionTest(
    override val dockerRuleFolder: String = "DS008",
    override val customDockerFiles: Set<String> = emptySet(),
    override val targetInspection: LocalInspectionTool = DockerfileWorkdirInspection()
) : DockerHighlightingBaseTest()