# Infrastructure Security Linter | IDEA Plugin

[![CI](https://github.com/NordCoderd/infrastructure-security/actions/workflows/gradle.yml/badge.svg)](https://github.com/NordCoderd/infrastructure-security/actions/workflows/gradle.yml)
[![JetBrains Plugin Version](https://img.shields.io/jetbrains/plugin/v/dev.protsenko.security-linter)](https://plugins.jetbrains.com/plugin/25413-infrastructure-security)
[![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/dev.protsenko.security-linter)](https://plugins.jetbrains.com/plugin/25413-infrastructure-security)

<!-- Plugin description -->
Infrastructure Security Linter for JetBrains IDEs (e.g., IntelliJ IDEA, PyCharm, WebStorm, and more).

Scan Docker and Infrastructure as Code (IaC) files for security vulnerabilities and misconfigurations directly in your JetBrains IDE.

## Why this plugin?

- Seamless integration into IDE without installing external tools.
- Verifies your files on the fly and highlight problems earlier and that make shift left happens.
- Quick-fixes for problems are available for some inspections that could help fix problem faster.
- Supports complicated verifications, such as tracking variables and arguments as sources of issues.
- Pure Kotlin implementation, leveraging the power of IDEs.

## What does the plugin offer?

- **Dockerfile Analysis**: Detect security vulnerabilities and optimize Docker images with over 40 checks.
- **Docker Compose**: Detect security vulnerabilities and misconfigurations.
- **Quick Fixes**: Resolve issues faster using built-in quick fixes.

## What problems could find that plugin?

Currently, documentation in progress and will be available soon. 
At that moment you could check [list of inspection messages](https://github.com/NordCoderd/infrastructure-security/blob/ae38c2e2a257d054329929c571e0a5daecfe1171/src/main/resources/messages/SecurityPluginBundle.properties#L20), they describe supported problems. 

## Planned features

- **Extended support for Dockerfile and docker-compose files**
- **Kubernetes Files**: Analyzing Kubernetes YAML files to comply with best practices and security standards.
- **and more**: Expanding support for other IaC tools and formats to comprehensively protect and optimize your infrastructure configurations.

Detailed list of planned features are available on [GitHub issues](https://github.com/NordCoderd/infrastructure-security/labels/enhancement)

## Thanks
- My mother, who supported me every step of the way and who is no longer with us.
- [Trivy-checks](https://github.com/aquasecurity/trivy-checks/tree/main) for good source of rules.
- [Hadolint](https://github.com/hadolint/hadolint) for yet another docker rule set.
<!-- Plugin description end -->
