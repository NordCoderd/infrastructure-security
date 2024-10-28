package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.workdir.DockerfileWorkdirInspection

class DS008WorkdirPathNotAbsoluteInspectionTest(
    override val ruleFolderName: String = "DS008",
    override val customFiles: Set<String> = emptySet(),
    override val targetInspection: LocalInspectionTool = DockerfileWorkdirInspection()
) : DockerHighlightingBaseTest()