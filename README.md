# jira-commit-message-intellij-plugin

Jira commit message plugin helps you by automatically appending JIRA task id to your commit messages.

![Build](https://github.com/nemwiz/jira-commit-message-intellij-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/14310-jira-id-commit-message.svg)](https://plugins.jetbrains.com/plugin/14310-jira-id-commit-message)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/14310-jira-id-commit-message.svg)](https://plugins.jetbrains.com/plugin/14310-jira-id-commit-message)

## Template ToDo list
- [x] Create a new [IntelliJ Platform Plugin Template][template] project.
- [x] Verify the [pluginGroup](/gradle.properties), [plugin ID](/src/main/resources/META-INF/plugin.xml) and [sources package](/src/main/kotlin).
- [x] Review the [Legal Agreements](https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html).
- [x] [Publish a plugin manually](https://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/publishing_plugin.html) for the first time.
- [x] Set the Plugin ID in the above README badges.
- [x] Set the [Deployment Token](https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html).
- [x] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.

<!-- Plugin description -->
A plugin that takes JIRA id from your branch name and appends it to the commit message.
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "JIRA id commit message"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/nemwiz/jira-commit-message-intellij-plugin/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---

### How to use the plugin


1. [Install the plugin](https://plugins.jetbrains.com/plugin/14310-jira-id-commit-message)
2. Go to File > Settings > Tools > JIRA Id Commit Message
3. Add your project prefix (e.g. your JIRA ticket is FROG-123, so your project prefix would be FROG)
![Example](screenshot1.JPG)

4. Name your branches in one of the following format. Just an example :)
    > feature/FROG-123
    >
    > feature/FROG-123-whatever
    >
    > FROG-123
    >
    > FROG-123-whatever
    >
    > whatever/feature/FROG-123-whatever

5. Make some changes. When the "Commit changes" dialog is opened your commit message is **automatically** populated with JIRA id from the branch name.
![Example](screenshot2.JPG)


6.(Optional) You can always regenerate the commit message by clicking on the frog icon.
![Example](screenshot3.JPG)

Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
