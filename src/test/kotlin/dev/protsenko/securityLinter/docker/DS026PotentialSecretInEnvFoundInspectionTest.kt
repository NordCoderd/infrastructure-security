package dev.protsenko.securityLinter.docker

import com.intellij.codeInspection.LocalInspectionTool
import dev.protsenko.securityLinter.core.DockerHighlightingBaseTest
import dev.protsenko.securityLinter.docker.inspection.env.DockerFileEnvInspection

class DS026PotentialSecretInEnvFoundInspectionTest(
    override val ruleFolderName: String = "DS026",
    override val customFiles: Set<String> = emptySet<String>(),
    override val targetInspection: LocalInspectionTool = DockerFileEnvInspection()
) : DockerHighlightingBaseTest()