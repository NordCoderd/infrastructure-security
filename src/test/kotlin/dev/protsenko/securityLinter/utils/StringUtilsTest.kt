package dev.protsenko.securityLinter.utils

import junit.framework.TestCase

class StringUtilsTest: TestCase() {

    fun testExtension(){
        val noExtension = "test".extension()
        val extension = "test.jpeg".extension()
        assertEquals("", noExtension)
        assertEquals("jpeg", extension)
    }

}