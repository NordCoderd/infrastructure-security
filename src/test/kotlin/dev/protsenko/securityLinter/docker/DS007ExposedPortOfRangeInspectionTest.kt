package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.expose.DockerfileExposeInspection

class DS007ExposedPortOfRangeInspectionTest(
    override val ruleFolderName: String = "DS007",
    override val customFiles: Set<String> = setOf<String>(
        "Dockerfile-multiple-invalid-ports-with-postfixes.denied",
        "Dockerfile-multiple-invalid-ports.denied",
    ),
    override val targetInspection: LocalInspectionTool = DockerfileExposeInspection()
) : DockerHighlightingBaseTest()