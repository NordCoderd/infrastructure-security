package dev.protsenko.securityLinter.core

object DockerFileConstants {
    val PROHIBITED_PORTS = setOf<String>("22", "22/tcp", "22/udp")
}