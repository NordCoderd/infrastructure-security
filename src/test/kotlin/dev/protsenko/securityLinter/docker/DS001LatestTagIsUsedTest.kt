package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.from.DockerfileFromInspection

class DS001LatestTagIsUsedTest(
    override val ruleFolderName: String = "DS001",
    override val customFiles: Set<String> = setOf(
        "Dockerfile-missed-version-tag.denied",
        "Dockerfile-with-slash.denied",
        "Dockerfile-with-args.denied",
        "Dockerfile-with-envs.denied",
        "Dockerfile-alias-of-valid-image.denied"
    ),
    override val targetInspection: LocalInspectionTool = DockerfileFromInspection()
) : DockerHighlightingBaseTest()