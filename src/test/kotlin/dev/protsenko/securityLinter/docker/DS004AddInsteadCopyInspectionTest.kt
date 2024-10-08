package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.copy_and_add.DockerfileCopyAndAddInspection

class DS004AddInsteadCopyInspectionTest(
    override val dockerRuleFolder: String = "DS004",
    override val customDockerFiles: Set<String> = setOf<String>(
        "Dockerfile-brackets.denied",
        "Dockerfile-empty-add.allowed"
    ),
    override val targetInspection: LocalInspectionTool = DockerfileCopyAndAddInspection()
) : DockerHighlightingBaseTest()