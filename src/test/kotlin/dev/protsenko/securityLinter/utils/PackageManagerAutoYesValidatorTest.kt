package dev.protsenko.securityLinter.utils

import dev.protsenko.securityLinter.docker.checker.PackageManagerAutoYesValidator
import junit.framework.TestCase


class PackageManagerAutoYesValidatorTest : TestCase() {

    fun testValidCommands() {
        val commands = listOf(
            // apt-get valid commands
            "RUN apt-get install -y package",
            "RUN apt-get -y install package",
            "RUN apt-get install package -y",
            "RUN apt-get install --yes package",
            "RUN apt-get install --assume-yes package",
            "RUN apt-get install -qy package",
            "RUN apt-get install -yq package",
            "RUN apt-get -qq -y install package",
            "RUN apt-get install package1 package2 -y",
            "RUN apt-get install -y package && \\\n apt-get install -y another-package",
            "RUN apt-get update && apt-get install -y package",
            "RUN apt-get install -y package1 && apt-get install -y package2",
            "RUN apt-get install -y package && echo 'Installation complete'",
            "RUN echo 'apt-get install package'",
            "RUN apt-get install -y package && apt-get clean",
            "RUN apt-get update; apt-get install -y package",

            // yum valid commands
            "RUN yum install -y package",
            "RUN yum -y install package",
            "RUN yum install package -y",
            "RUN yum install --assumeyes package",
            "RUN yum install -y package && yum clean all",

            // dnf valid commands
            "RUN dnf install -y package",
            "RUN dnf -y install package",
            "RUN dnf install package -y",
            "RUN dnf install --assumeyes package",
            "RUN dnf install -y package && dnf clean all",

            // zypper valid commands
            "RUN zypper install -y package",
            "RUN zypper -y install package",
            "RUN zypper install package -y",
            "RUN zypper install --non-interactive package",
            "RUN zypper install -y package && zypper clean"
        )
        for (command in commands) {
            assertTrue(
                "Command '$command' should be valid",
                PackageManagerAutoYesValidator.isValid(command)
            )
        }
    }

    fun testInvalidCommands() {
        val commands = listOf(
            // apt-get invalid commands
            "RUN apt-get install package",
            "RUN apt-get install package1 package2",
            "RUN apt-get -qq install package",
            "RUN apt-get install package && apt-get clean",
            "RUN apt-get install package --no-install-recommends",
            "RUN apt-get update && apt-get install package",
            "RUN apt-get install -y package1 && apt-get install package2",
            "RUN apt-get install package1 && apt-get install -y package2",
            "RUN apt-get update && apt-get install package && apt-get install -y another-package",
            "RUN apt-get install -y package && apt-get install package2",
            "RUN apt-get update; apt-get install package",
            "RUN apt-get install package && echo 'Done'",
            "RUN apt-get -y install package && apt-get install package2",

            // yum invalid commands
            "RUN yum install package",
            "RUN yum install package && yum clean all",
            "RUN yum update && yum install package",
            "RUN yum install -y package1 && yum install package2",

            // dnf invalid commands
            "RUN dnf install package",
            "RUN dnf install package && dnf clean all",
            "RUN dnf update && dnf install package",
            "RUN dnf install -y package1 && dnf install package2",

            // zypper invalid commands
            "RUN zypper install package",
            "RUN zypper install package && zypper clean",
            "RUN zypper update && zypper install package",
            "RUN zypper install -y package1 && zypper install package2"
        )
        for (command in commands) {
            assertFalse(
                "Command '$command' should be invalid",
                PackageManagerAutoYesValidator.isValid(command)
            )
        }
    }
}

