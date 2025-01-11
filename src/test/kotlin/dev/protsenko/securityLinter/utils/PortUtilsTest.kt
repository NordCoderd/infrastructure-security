package dev.protsenko.securityLinter.utils

import junit.framework.TestCase

class PortUtilsTest : TestCase() {

    /**
     * A single test method covering multiple scenarios for container port parsing.
     */
    fun testParseContainerPortsMerged() {
        // 1) Single part: only container port
        assertEquals(
            "Single part (\"3000\") should be interpreted as [3000]",
            listOf(3000),
            PortUtils.parseContainerPorts("3000")
        )

        // 2) Host:container
        assertEquals(
            "Should parse container port as [3000]",
            listOf(3000),
            PortUtils.parseContainerPorts("8000:3000")
        )

        // 3) Host IP:host:container
        assertEquals(
            "Should parse container port as [3000], ignoring IP and host part",
            listOf(3000),
            PortUtils.parseContainerPorts("127.0.0.1:8000:3000")
        )

        // 4) Ranged host -> ranged container
        assertEquals(
            "Should parse container range as [8080, 8081]",
            listOf(8080, 8081),
            PortUtils.parseContainerPorts("9090-9091:8080-8081")
        )

        // 5) Protocol suffix
        assertEquals(
            "Should ignore /udp and parse container port as [6060]",
            listOf(6060),
            PortUtils.parseContainerPorts("6060:6060/udp")
        )

        // 6) Invalid input
        assertTrue(
            "Invalid input should return empty list",
            PortUtils.parseContainerPorts("not-valid").isEmpty()
        )

        // 7) IP + ranged container
        assertEquals(
            "Should ignore IP and host range, parsing container range [3000, 3001, 3002, 3003]",
            listOf(3000, 3001, 3002, 3003),
            PortUtils.parseContainerPorts("127.0.0.1:8000-8003:3000-3003")
        )
    }

}