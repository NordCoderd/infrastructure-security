package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.user.DockerfileUserInspection

class DS002MissedOrRootUserIsUsedInspectionTest(
    override val dockerRuleFolder: String = "DS002",
    override val customDockerFiles: Set<String> = setOf(
        "Dockerfile-root-declared.denied",
        "Dockerfile-multistage.denied",
        "Dockerfile-multistage-2.denied",
        "Dockerfile-root-user-from-args.denied",
        "Dockerfile-user-from-args.denied"
    ),
    override val targetInspection: LocalInspectionTool = DockerfileUserInspection()
) : DockerHighlightingBaseTest()