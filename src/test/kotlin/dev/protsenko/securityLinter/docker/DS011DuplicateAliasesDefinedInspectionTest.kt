package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.DS011DuplicateAliasesDefinedInspection

class DS011DuplicateAliasesDefinedInspectionTest(
    override val dockerRuleFolder: String = "DS011",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS011DuplicateAliasesDefinedInspection()
) : DockerHighlightingBaseTest()