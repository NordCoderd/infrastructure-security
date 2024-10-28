package dev.protsenko.securityLinter.core

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.testFramework.TestDataPath
import com.intellij.testFramework.fixtures.BasePlatformTestCase

@TestDataPath("\$CONTENT_ROOT/src/test/testData")
abstract class InspectionBaseTest(): BasePlatformTestCase() {
    /**
     * Rule folder name
     */
    abstract val ruleFolderName: String

    /**
     * Original file name to testing highlighting
     */
    abstract val targetFileName: String

    /**
     * Expected file type
     */
    abstract val expectedFile: Class<*>

    /**
     * Set of custom files used to test different test-cases
     */
    abstract val customFiles: Set<String>

    /**
     * Inspection that should be tested
     */
    abstract val targetInspection: LocalInspectionTool

    /**
     * Test data path
     */
    abstract override fun getTestDataPath(): String

    override fun setUp(){
        super.setUp()
        myFixture.enableInspections(targetInspection::class.java)
    }

    /**
     * Used for testing file with specified issue
     */
    open fun testDeniedDockerFile(){
        setupBaseForRunningInspection("$targetFileName.denied")
        checkHighlighting()
    }

    /**
     * Used for testing file without issues
     */
    fun testAllowedDockerFile(){
        setupBaseForRunningInspection("$targetFileName.allowed")
        checkHighlighting()
    }

    /**
     * Used for testing custom files
     */
    fun testCustomDockerFiles(){
        if (customFiles.isEmpty()){
            return
        }
        for (customFile in customFiles) {
            setupBaseForRunningInspection(customFile)
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

    protected fun setupBaseForRunningInspection(customFile: String){
        assertNotNull(myFixture)
        val sourceDockerFileLocation = "$ruleFolderName/$customFile"
        val targetDockerFileLocation = "$ruleFolderName/$customFile/$targetFileName"

        val virtualFile = myFixture.copyFileToProject(sourceDockerFileLocation, targetDockerFileLocation)
        assertNotNull(virtualFile)

        val analyzedFile = myFixture.configureByFile(targetDockerFileLocation)
        assertNotNull(analyzedFile)

        assertInstanceOf(analyzedFile, expectedFile)
    }
}