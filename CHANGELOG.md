<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Infrastructure Security Linter Changelog

## [Unreleased]

## [1.0.3] - 01-10-2024

### Changed

- Improved tracking image name specified by arguments
- Added zypper, dnf, yum auto confirm checks
- Zypper dist-upgrade checks
- Use arguments JSON notation for CMD and ENTRYPOINT arguments

## [1.0.2] - 01-10-2024

### Added

- Looking for secrets in environment variables
- Looking for curl bashing
- Looking for unsafe RUN calls with dynamic arguments 
- Looking for apt-get without --no-install-recommends
- RUN inspections works with JSON notation

### Changed

- Most of RUN inspections were merge to one and moved to extensions
- USER command now tracking ARGS variables
- Updated highlighting types
- Higher supported IDE version now is 243
- Improved FROM parser for supporting image names with slash

## [1.0.1] - 15-09-2024

### Changed

- Added 23 docker inspections with actionable quick fixes