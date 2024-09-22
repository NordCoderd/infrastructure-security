package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.DS016PackageManagerUpdateWithoutInstallInspection

class DS016PackageManagerUpdateWithoutInstallInspectionTest(
    override val dockerRuleFolder: String = "DS016",
    override val customDockerFiles: Set<String> = setOf("Dockerfile-run-multiline.allowed"),
    override val targetInspection: LocalInspectionTool = DS016PackageManagerUpdateWithoutInstallInspection()
) : DockerHighlightingBaseTest()