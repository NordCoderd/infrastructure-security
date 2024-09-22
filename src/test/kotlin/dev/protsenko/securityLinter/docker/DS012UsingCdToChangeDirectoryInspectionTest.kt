package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest

class DS011DuplicateAliasesDefinedInspectionTest(
    override val dockerRuleFolder: String = "DS011",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS011DuplicateAliasesDefinedInspection()
) : DockerHighlightingBaseTest()