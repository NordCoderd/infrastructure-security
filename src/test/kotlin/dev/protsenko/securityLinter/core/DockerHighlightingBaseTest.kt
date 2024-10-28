package dev.protsenko.securityLinter.core

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.docker.dockerFile.DockerPsiFile
import com.intellij.testFramework.TestDataPath

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
abstract class DockerHighlightingBaseTest(): InspectionBaseTest() {
    override fun getTestDataPath() = "src/test/testData/docker"

    override val targetFileName = "Dockerfile"
    override val expectedFile: Class<*> = DockerPsiFile::class.java

    abstract override val targetInspection: LocalInspectionTool
}