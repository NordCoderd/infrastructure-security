<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Infrastructure Security Linter Changelog

## [1.0.6] 11-01-2025

### Added
- docker-compose support: exposing insecure ports

## [1.0.5] 04-01-2025

### Added
- docker-compose support: using root user
- missing HEALTHCHECK instruction
- using apt instead apt-get or apt-cache

### Fixed
- bug with removing stage name after using quick fix


## [1.0.4] 28-10-2024

### Added
- docker-compose support: using unsafe images

## [1.0.3] 06-10-2024

### Added
- Added zypper, dnf, yum auto-confirm checks
- Added additional zypper dist-upgrade check
- Use arguments JSON notation for CMD and ENTRYPOINT arguments

### Changed

- Improved tracking image name specified by arguments
- Inspections merged by Dockerfile instructions
- Improved thread-safety for complex inspections

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