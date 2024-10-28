package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.run.DockerfileRunInspection

class DS009SudoIsUsedInspectionTest(
    override val ruleFolderName: String = "DS009",
    override val customFiles: Set<String> = emptySet(),
    override val targetInspection: LocalInspectionTool = DockerfileRunInspection()
) : DockerHighlightingBaseTest()