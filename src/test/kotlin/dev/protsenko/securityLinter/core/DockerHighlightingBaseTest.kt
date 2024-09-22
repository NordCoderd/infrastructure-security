package dev.protsenko.securityLinter.core

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.docker.dockerFile.DockerPsiFile
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
abstract class DockerHighlightingBaseTest(): BasePlatformTestCase() {

    /**
     * Docker rule folder should contain two files: Dockerfile.(allowed/denied)
     */
    abstract val dockerRuleFolder: String

    /**
     * Set of custom docker files used to test different test-cases
     */
    abstract val customDockerFiles: Set<String>

    /**
     * Inspection that should be tested
     */
    abstract val targetInspection: LocalInspectionTool

    override fun getTestDataPath() = "src/test/testData/docker"

    override fun setUp(){
        super.setUp()
        myFixture.enableInspections(targetInspection::class.java)
    }

    /**
     * Used for testing docker file with specified issue
     */
    fun testDeniedDockerFile(){
        setupBaseForRunningInspection(DockerTestStringLiterals.DENIED_DOCKER_FILE)
        checkHighlighting()
    }

    /**
     * Used for testing docker file without issues
     */
    fun testAllowedDockerFile(){
        setupBaseForRunningInspection(DockerTestStringLiterals.ALLOWED_DOCKER_FILE)
        checkHighlighting()
    }

    /**
     * Used for testing custom docker files
     */
    fun testCustomDockerFiles(){
        if (customDockerFiles.isEmpty()){
            return
        }
        for (dockerFile in customDockerFiles) {
            setupBaseForRunningInspection(dockerFile)
            checkHighlighting()
        }
    }

    /**
     * Final step to check highlighting of configured files
     */
    protected fun checkHighlighting(){
        assertNotNull(myFixture)
        myFixture.checkHighlighting()
    }

    protected fun setupBaseForRunningInspection(dockerFile: String){
        assertNotNull(myFixture)
        val sourceDockerFileLocation = "$dockerRuleFolder/$dockerFile"
        val targetDockerFileLocation = "$dockerRuleFolder/$dockerFile/Dockerfile"

        val virtualDockerFile = myFixture.copyFileToProject(sourceDockerFileLocation, targetDockerFileLocation)
        assertNotNull(virtualDockerFile)

        val analyzedFile = myFixture.configureByFile(targetDockerFileLocation)
        assertNotNull(analyzedFile)

        assertInstanceOf(analyzedFile, DockerPsiFile::class.java)
    }
}