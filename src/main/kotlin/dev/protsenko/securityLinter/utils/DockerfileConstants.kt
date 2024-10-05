package dev.protsenko.securityLinter.utils

object DockerfileConstants{
    val PROHIBITED_USERS = setOf("root", "0")
    val POTENTIAL_SECRETS_NAME = setOf<String>(
        "PASSWD",
        "PASSWORD",
        "PASS",
        "SECRET",
        "KEY",
        "ACCESS",
        "API_KEY",
        "APIKEY",
        "TOKEN",
        "TKN"
    )
}