package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.DS002MissedOrRootUserIsUsedInspection

class DS002MissedOrRootUserIsUsedInspectionTest(
    override val dockerRuleFolder: String = "DS002",
    override val customDockerFiles: Set<String> = setOf("Dockerfile-root-declared.denied"),
    override val targetInspection: LocalInspectionTool = DS002MissedOrRootUserIsUsedInspection()
) : DockerHighlightingBaseTest()