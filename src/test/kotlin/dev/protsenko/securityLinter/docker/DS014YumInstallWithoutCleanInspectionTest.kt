package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS013UsingWgetAndCurlBothInspectionTest(
    override val dockerRuleFolder: String = "DS013",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS013UsingWgetAndCurlBothInspection()
) : DockerHighlightingBaseTest()