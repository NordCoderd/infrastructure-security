# Infrastructure Security Plugin

[![CI](https://github.com/NordCoderd/infrastructure-security/actions/workflows/gradle.yml/badge.svg)](https://github.com/NordCoderd/infrastructure-security/actions/workflows/gradle.yml)
[![JetBrains Plugin Version](https://img.shields.io/jetbrains/plugin/v/dev.protsenko.security-linter)](https://plugins.jetbrains.com/plugin/25413-infrastructure-security)
[![JetBrains Plugin Downloads](https://img.shields.io/jetbrains/plugin/d/dev.protsenko.security-linter)](https://plugins.jetbrains.com/plugin/25413-infrastructure-security)

<!-- Plugin description -->
Plugin for JetBrains IDEs provides an easy and effective way to identify security issues (misconfigurations) and adhere to best practices for Infrastructure as Code (IaC) files.

## Features

- **Dockerfile Analysis**: Includes 23 inspections aimed at detecting vulnerabilities and optimizing Docker images.
- **Quick Fixes**: Includes quick fixes for faster resolving problems

## Planned Features

- **Docker Compose**: Checking `docker-compose` configuration files to ensure security and efficiency.
- **Kubernetes Files**: Analyzing Kubernetes YAML files to comply with best practices and security standards.
- **and more**: Expanding support for other IaC tools and formats to comprehensively protect and optimize your infrastructure configurations.

## Thanks
- [Trivy-checks](https://github.com/aquasecurity/trivy-checks/tree/main) for providing a good starting point by porting their Dockerfile rules.
<!-- Plugin description end -->
