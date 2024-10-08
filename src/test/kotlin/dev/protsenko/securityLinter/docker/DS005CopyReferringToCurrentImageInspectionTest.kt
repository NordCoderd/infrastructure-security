package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.copy_and_add.DockerfileCopyAndAddInspection

class DS005CopyReferringToCurrentImageInspectionTest(
    override val dockerRuleFolder: String = "DS005",
    override val customDockerFiles: Set<String> = setOf("Dockerfile-multi-step.denied"),
    override val targetInspection: LocalInspectionTool = DockerfileCopyAndAddInspection()
) : DockerHighlightingBaseTest()