package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.env.DS026PotentialSecretInEnvFoundInspection

class DS026PotentialSecretInEnvFoundInspectionTest(
    override val dockerRuleFolder: String = "DS026",
    override val customDockerFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DS026PotentialSecretInEnvFoundInspection()
) : DockerHighlightingBaseTest()