<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# IntelliJ Platform Plugin Template Changelog

## [Unreleased]

## [1.0.2]

### Added

- Looking for secrets in environment variables
- Looking for curl bashing
- Looking for unsafe RUN calls with dynamic arguments 
- Looking for apt-get without --no-install-recommends
- RUN inspections works with JSON notation

### Changed

- Most of RUN inspections were merge to one and moved to extensions
- User tracking now checks ARGS variables
- Updated highlighting types
- Higher supported IDE version now is 243
- Improved FROM parser

## [1.0.1] - 2024-15-09

### Changed

- Added 23 docker inspections with actionable quick fixes