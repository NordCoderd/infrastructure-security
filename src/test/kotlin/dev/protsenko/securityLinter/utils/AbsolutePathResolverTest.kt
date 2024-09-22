package dev.protsenko.securityLinter.utils

import junit.framework.TestCase

class AbsolutePathResolverTest : TestCase() {

    fun testValidUnixPaths() {
        assertTrue(AbsolutePathResolver.isAbsolutePath("/"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("/usr/app"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("/path/to/workdir"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("\"/path/to/workdir\""))
    }

    fun testValidWindowsPaths() {
        assertTrue(AbsolutePathResolver.isAbsolutePath("C:\\app"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("C:\\path\\to\\workdir"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("D:\\folder\\subfolder"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("\"C:\\app\""))
    }

    fun testValidEnvironmentVariablePaths() {
        assertTrue(AbsolutePathResolver.isAbsolutePath("\$HOME/app"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("\${HOME}/app"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("\$VARIABLE"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("\$VARIABLE/path"))
        assertTrue(AbsolutePathResolver.isAbsolutePath("\${VARIABLE}/path"))
    }

    fun testInvalidPaths() {
        assertFalse(AbsolutePathResolver.isAbsolutePath("app"))
        assertFalse(AbsolutePathResolver.isAbsolutePath("../app"))
        assertFalse(AbsolutePathResolver.isAbsolutePath("./app"))
        assertFalse(AbsolutePathResolver.isAbsolutePath("\"app\""))
        assertFalse(AbsolutePathResolver.isAbsolutePath("relative/path"))
        assertFalse(AbsolutePathResolver.isAbsolutePath("~/path"))
        assertFalse(AbsolutePathResolver.isAbsolutePath("C/path"))
    }
}