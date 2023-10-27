<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# jira-commit-message Changelog

## [Unreleased]

## [3.4.1] - 2023-10-27

### Changed
- Fixes an IndexOutOfBounds exception when opening a project [#77](https://github.com/nemwiz/jira-commit-message-intellij-plugin/issues/77)

## [3.4.0] - 2023-08-15

### Added
- Prepend JIRA issue to existing commit message when clicking plugin action button [#74](https://github.com/nemwiz/jira-commit-message-intellij-plugin/issues/74)
- Always uppercase JIRA project key even when branch is lowercase [#70](https://github.com/nemwiz/jira-commit-message-intellij-plugin/issues/70)

### Changed
- Conventional commit will only be picked up if a branch name starts with a conventional commit prefix [#71](https://github.com/nemwiz/jira-commit-message-intellij-plugin/issues/71)

## [3.3.0] - 2023-05-31

### Added
- Support for conventional commits based on branch name
- Ability to specify message prefix

## [3.2.0] - 2023-01-27

### Added
- Option to automatically detect JIRA project key

## [3.1.0] - 2023-01-13

### Added
- Ability to specify message infix
- When switching branches the commit message is updated on the commit tool window

### Changed
- Removes deprecated APIs
- Updates plugin libraries to latest
- Unit tests

## [3.0.1]

### Changed
- Bumps plugin to support the latest version of IntelliJ

## [3.0.0]

### Added
- Ability to specify multiple prefixes

### Changed
- Plugin settings are now global instead of per project

## [2.2.0]

## [2.1.5]

### Added
- Shows a notification when plugin is not configured properly
- Support for IntelliJ version 2022.1

## [2.1.4]

## [2.1.3]

## [2.1.2]

## [2.1.1]

## [2.1.0]

### Added
- Ability to configure brackets for commit messages

### Changed
- Upgraded plugin template to use latest version of libraries

## [2.0.0]

### Added
- Support up until IntelliJ version 2020.2
- Migrated project to [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)

[Unreleased]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v3.4.1...HEAD
[3.4.1]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v3.4.0...v3.4.1
[3.4.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v3.3.0...v3.4.0
[3.3.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v3.2.0...v3.3.0
[3.2.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v3.1.0...v3.2.0
[3.1.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v3.0.1...v3.1.0
[3.0.1]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v3.0.0...v3.0.1
[3.0.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.2.0...v3.0.0
[2.2.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.1.5...v2.2.0
[2.1.5]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.1.4...v2.1.5
[2.1.4]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.1.3...v2.1.4
[2.1.3]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.1.2...v2.1.3
[2.1.2]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.1.1...v2.1.2
[2.1.1]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.1.0...v2.1.1
[2.1.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/compare/v2.0.0...v2.1.0
[2.0.0]: https://github.com/nemwiz/jira-commit-message-intellij-plugin/commits/v2.0.0
