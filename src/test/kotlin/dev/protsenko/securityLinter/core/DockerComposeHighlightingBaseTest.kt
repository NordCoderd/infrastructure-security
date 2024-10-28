package dev.protsenko.securityLinter.core

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.testFramework.TestDataPath
import org.jetbrains.yaml.psi.YAMLFile

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
abstract class DockerComposeHighlightingBaseTest(): InspectionBaseTest() {
    override fun getTestDataPath() = "src/test/testData/docker_compose"

    override val targetFileName = "docker-compose.yml"
    override val expectedFile: Class<*> = YAMLFile::class.java

    abstract override val targetInspection: LocalInspectionTool
}