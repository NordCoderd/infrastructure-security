package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.healthcheck.DockerfileHealthCheckInspection

class DS021MultipleHealthCheckDefinedInspectionTest(
    override val ruleFolderName: String = "DS021",
    override val customFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DockerfileHealthCheckInspection()
) : DockerHighlightingBaseTest()