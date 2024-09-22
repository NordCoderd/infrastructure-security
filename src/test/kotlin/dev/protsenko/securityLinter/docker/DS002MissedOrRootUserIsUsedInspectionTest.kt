package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS001LatestTagIsUsedTest(
    override val dockerRuleFolder: String = "DS001",
    override val customDockerFiles: Set<String> = setOf("Dockerfile-missed-version-tag.denied"),
    override val targetInspection: LocalInspectionTool = DS001LatestTagIsUsedInspection()
) : DockerHighlightingBaseTest()