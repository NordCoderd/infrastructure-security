package dev.protsenko.securityLinter.core

object DockerFileInspectionConstants {
    const val COPY_FROM_OPTION_NAME = "from"

    val PROHIBITED_USERS = setOf<String>("root")
    val PROHIBITED_PORTS = setOf<String>("22", "22/tcp", "22/udp")
}