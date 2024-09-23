package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.expose.DS007ExposedPortOutOfRangeInspection

class DS007ExposedPortOfRangeInspectionTest(
    override val dockerRuleFolder: String = "DS007",
    override val customDockerFiles: Set<String> = setOf<String>(
        "Dockerfile-multiple-invalid-ports-with-postfixes.denied",
        "Dockerfile-multiple-invalid-ports.denied",
    ),
    override val targetInspection: LocalInspectionTool = DS007ExposedPortOutOfRangeInspection()
) : DockerHighlightingBaseTest()