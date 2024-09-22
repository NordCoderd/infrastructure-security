package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.DS013UsingWgetAndCurlBothInspection

class DS013UsingWgetAndCurlBothInspectionTest(
    override val dockerRuleFolder: String = "DS013",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS013UsingWgetAndCurlBothInspection()
) : DockerHighlightingBaseTest()