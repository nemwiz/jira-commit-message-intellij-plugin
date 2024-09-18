# jira-commit-message-intellij-plugin

Jira commit message plugin helps you by automatically appending JIRA task id to your commit messages.

![Build](https://github.com/nemwiz/jira-commit-message-intellij-plugin/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/14310-jira-id-commit-message.svg)](https://plugins.jetbrains.com/plugin/14310-jira-id-commit-message)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/14310-jira-id-commit-message.svg)](https://plugins.jetbrains.com/plugin/14310-jira-id-commit-message)

<!-- Plugin description -->
A simple plugin that takes JIRA issue from your branch name and appends it to the commit message.
Supports both commit dialog and commit tool window. Optional support for conventional commits.

This plugin works only with Git version control system.
<!-- Plugin description end -->

### Installation

- Using IDE built-in plugin system:
  
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "JIRA id commit message"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/nemwiz/jira-commit-message-intellij-plugin/releases/latest) and install it manually using
  <kbd>Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---

### Configuration options

You can configure the plugin under `Go to File > Settings > Tools > JIRA Id Commit Message`.

The plugin supports two modes:

1. [Automatic](#automatic)
2. [Manual](#manual)

For more options see [additional configuration](#additional-configuration)

#### Automatic

When you select `Automatically detect JIRA project key`, the plugin will infer
your project key from the branch name. Here are some examples:

|        Your branch name         |  Commit message produced by the plugin  |
|:-------------------------------:|:---------------------------------------:|
|  feat/this-PRODUCT-123-is-cool  |               PRODUCT-123               |
|          fix/LEGOS-541          |                LEGOS-541                |
|       MARCOM_881-feature        |               MARCOM_881                |

#### Manual

If you want to have more control over which project keys the plugin will recognize,
you can specify your project keys. Multiple keys are supported. 
The project key has to be specified in ***uppercase*** as per [Atlassian convention](https://confluence.atlassian.com/adminjiraserver/changing-the-project-key-format-938847081.html).

To configure it, unselect `Automatically detect JIRA project key` and add the project keys.
Here is an example configuration:

|  Configured project key  |        Your branch name         |  Commit message produced by the plugin  |
|:------------------------:|:-------------------------------:|:---------------------------------------:|
|         PRODUCT          |  feat/this-PRODUCT-123-is-cool  |               PRODUCT-123               |
|          LEGOS           |          fix/LEGOS-541          |                LEGOS-541                |
|          MARCOM          |       MARCOM_881-feature        |               MARCOM_881                |

#### Conventional commits support

Some projects follow [Conventional commits](https://www.conventionalcommits.org/en/v1.0.0/#summary) for easier
version management of their projects. You can enable it in the settings screen.

When configured, the plugin will automatically detect if your branch name starts with any of the conventional commit prefixes 
and it will add it to the commit message. Always place the conventional commit prefix at the beginning of the branch name. 
Here are some examples:

|       Your branch name        | Commit message produced by the plugin |
|:-----------------------------:|:-------------------------------------:|
| feat/this-PRODUCT-123-is-cool |          feat(PRODUCT-123):           |
|         fix/LEGOS-541         |            fix(LEGOS-541):            |
|    docs-MARCOM_881-feature    |           docs(MARCOM_881):           |

### Additional configuration

It's possible to specify how the final commit message will look like. 
Here are some options:

- Bracket/wrapper type - brackets that enclose your commit message. Some examples:
  - (PRODUCT-123)
  - {LEGOS-456}
  - \LEGOS-456\
- Message prefix - prefix that is added at the beginning of your commit message. Some examples:
  - #PRODUCT-123
  - :PRODUCT-123
  - -(LEGOS-456)
- Message infix - infix that is appended at the end of your commit message. Some examples:
  - PRODUCT-123:
  - (PRODUCT-123):
  - LEGOS-456 -
- Prepend JIRA issue to existing commit message on plugin action click. See [#74](https://github.com/nemwiz/jira-commit-message-intellij-plugin/issues/74)
- Populate the commit message with a keyboard shortcut. See [#84](https://github.com/nemwiz/jira-commit-message-intellij-plugin/issues/84)


### How to use the plugin

Regardless if you are using the commit dialog or commit tool window, the plugin will automatically populate your commit message text box.

In scenarios where you accidentally delete the text, you can always re-generate the message by clicking the frog icon.
See below screenshot.

![Example](screenshot3.JPG)

Plugin based on the [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template).
