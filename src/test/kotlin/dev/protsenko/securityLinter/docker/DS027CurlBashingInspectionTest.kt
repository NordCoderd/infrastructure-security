package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DS027CurlBashingInspection

class DS027CurlBashingInspectionTest(
    override val dockerRuleFolder: String = "DS027",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS027CurlBashingInspection()
) : DockerHighlightingBaseTest()