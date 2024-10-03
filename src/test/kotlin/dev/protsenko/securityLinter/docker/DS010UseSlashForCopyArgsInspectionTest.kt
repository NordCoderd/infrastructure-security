package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.copy_or_add.DockerfileCopyOrAddInspection

class DS010UseSlashForCopyArgsInspectionTest(
    override val dockerRuleFolder: String = "DS010",
    override val customDockerFiles: Set<String> = setOf<String>(
        "Dockerfile-multiply-without-brackets.denied",
        "Dockerfile-multiply-without-brackets.allowed",
        "Dockerfile-copy-with-arguments.allowed",
        "Dockerfile-common.allowed",
        "Dockerfile-json-common.allowed"
    ),
    override val targetInspection: LocalInspectionTool = DockerfileCopyOrAddInspection()
) : DockerHighlightingBaseTest()