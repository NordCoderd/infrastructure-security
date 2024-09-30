# Infrastructure Security Linter | IDEA Plugin

[![CI](https://github.com/NordCoderd/infrastructure-security/actions/workflows/gradle.yml/badge.svg)](https://github.com/NordCoderd/infrastructure-security/actions/workflows/gradle.yml)
[![JetBrains Plugin Version](https://img.shields.io/jetbrains/plugin/v/dev.protsenko.security-linter)](https://plugins.jetbrains.com/plugin/25413-infrastructure-security)
[![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/dev.protsenko.security-linter)](https://plugins.jetbrains.com/plugin/25413-infrastructure-security)

<!-- Plugin description -->
Infrastructure Security Linter Plugin for JetBrains IDEs (e.g., IntelliJ IDEA, PyCharm, and more).

Scan Docker and Infrastructure as Code (IaC) files for security vulnerabilities and misconfigurations directly in your JetBrains IDE.

## Features

- **Dockerfile Analysis**: Detect security vulnerabilities and optimize Docker images with over 30 checks.
- **Quick Fixes**: Resolve issues faster using built-in quick fixes.

## How to use / install
1. Go to Settings - Plugins - Infrastructure Security Linter in your IDE.
2. Install it.
3. ???
4. Problems will be highlighted. Just open your Dockerfile's.

Make sure you have installed Docker plugin from JetBrains as it in dependencies.

## Planned Features

- **Extended support Dockerfile**
- **Docker Compose**: Checking `docker-compose` configuration files to ensure security and efficiency.
- **Kubernetes Files**: Analyzing Kubernetes YAML files to comply with best practices and security standards.
- **and more**: Expanding support for other IaC tools and formats to comprehensively protect and optimize your infrastructure configurations.

Detailed list of planned features are available on [GitHub issues](https://github.com/NordCoderd/infrastructure-security/labels/enhancement)

## Thanks
- [Trivy-checks](https://github.com/aquasecurity/trivy-checks/tree/main) for good source of rules.
<!-- Plugin description end -->
