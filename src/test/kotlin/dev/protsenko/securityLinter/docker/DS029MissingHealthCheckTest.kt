package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.healthcheck.DockerfileHealthCheckInspection

class DS029MissingHealthCheckTest(
    override val ruleFolderName: String = "DS029",
    override val customFiles: Set<String> = setOf("Dockerfile-in-the-last-stage.denied"),
    override val targetInspection: LocalInspectionTool = DockerfileHealthCheckInspection()
) : DockerHighlightingBaseTest()