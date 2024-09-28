package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DockerfileRunInspection

class DS019AptGetInstallWithoutAutoConfirmInspectionTest(
    override val dockerRuleFolder: String = "DS019",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DockerfileRunInspection()
) : DockerHighlightingBaseTest()